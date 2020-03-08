package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.error.exception.ApplyNotFoundException;
import com.eskiiimo.api.error.exception.ProjectNotFoundException;
import com.eskiiimo.api.error.exception.YouAreNotReaderException;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApply;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApplyAnswer;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import com.eskiiimo.api.error.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectApplyService {

    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectApplyRepository projectApplyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void addLeader(Project project, String userId){
        User user =  userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.LEADER)
                .user(user)
                .project(project)
                .build();
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }
    @Transactional
    public boolean applyProject(Long projectId, ProjectApplyDto apply, String visitorId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        User user =  userRepository.findByUserId(visitorId)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProjectApply projectApplyEntity = apply.toEntity(user);
        project.getApplies().add(projectApplyEntity);
        this.projectApplyRepository.save(projectApplyEntity);
        this.projectRepository.save(project);
        return Boolean.TRUE;
    }
    @Transactional
    public boolean updateApply(Long projectId, ProjectApplyDto apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        ProjectApply projectApply = projectApplyRepository.findByUser_UserId(visitorId)
                .orElseThrow(()->new ApplyNotFoundException("지원정보가 존재하지 않습니다."));
        project.getApplies().remove(projectApply);
        projectApply.setSelfDescription(apply.getSelfDescription());
        projectApply.setRole(apply.getRole());
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for(String answer : apply.getAnswers())
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());
        projectApply.setAnswers(answers);
        project.getApplies().add(projectApply);
        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
        return Boolean.TRUE;
    }
    @Transactional
    public List<ProjectApplicantDto> getApplicants(Long projectId, String visitorId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(!this.isLeader(project,visitorId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
            List<ProjectApplicantDto> applicants = new ArrayList<ProjectApplicantDto>();

            for(ProjectApply projectApply : project.getApplies()){
                ProjectApplicantDto projectApplicantDto =ProjectApplicantDto.builder()
                        .status(projectApply.getStatus())
                        .userId(projectApply.getUser().getUserId())
                        .userName(projectApply.getUser().getUserName())
                        .role(projectApply.getRole())
                        .build();
                applicants.add(projectApplicantDto);
            }
            return applicants;
    }
    @Transactional
    public ProjectApplyDto getApply(Long projectId, String userId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(this.isLeader(project,visitorId)) {
            for(ProjectApply projectApply : project.getApplies()){
                if(projectApply.getUser().getUserId().equals(userId)){
                    if(projectApply.getStatus()==ProjectApplyStatus.UNREAD)
                        projectApply.setStatus(ProjectApplyStatus.READ);
                    projectApplyRepository.save(projectApply);
                    ProjectApplyDto projectApplyDto = ProjectApplyDto.builder()
                            .userName(projectApply.getUser().getUsername())
                            .questions(project.getQuestions())
                            .answers(projectApply.getAnswers())
                            .selfDescription(projectApply.getSelfDescription())
                            .status(projectApply.getStatus())
                            .role(projectApply.getRole())
                            .build();
                    this.projectRepository.save(project);
                    return projectApplyDto;
                }
            }
        }
        return null;
    }
    @Transactional
    public Boolean acceptApply(Long projectId, String userId, String visitorId){
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(isLeader(project,visitorId)){
            ProjectRole memberRole=null;
            for(ProjectApply projectApply : project.getApplies()){
                if(projectApply.getUser().getUserId().equals(userId)){
                    project.getApplies().remove(projectApply);
                    memberRole= projectApply.getRole();
                    projectApply.setStatus(ProjectApplyStatus.ACCEPT);
                    project.getApplies().add(projectApply);
                    this.projectApplyRepository.save(projectApply);
                    break;
                }
            }
            Optional<User> optionalUser = userRepository.findByUserId(userId);
            if(optionalUser.isEmpty())
                return Boolean.FALSE;
            User user = optionalUser.get();
            ProjectMember projectMember = ProjectMember.builder()
                    .role(memberRole)
                    .user(user)
                    .project(project)
                    .build();
            project.getProjectMembers().add(projectMember);
            this.projectMemberRepository.save(projectMember);
            this.projectRepository.save(project);
            return Boolean.TRUE;
        }
        else
            return Boolean.FALSE;
    }
    @Transactional
    public Boolean rejectApply(Long projectId, String userId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(isLeader(project,visitorId)) {
            for (ProjectApply projectApply : project.getApplies()) {
                if (projectApply.getUser().getUserId().equals(userId)) {
                    projectApply.setStatus(ProjectApplyStatus.REJECT);
                    this.projectApplyRepository.save(projectApply);
                    this.projectRepository.save(project);
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    public Boolean isLeader(Project project,String visitorId){
        for(ProjectMember projectMember : project.getProjectMembers()){
            if(projectMember.getRole().equals(ProjectRole.LEADER)){
                if(projectMember.getUser().getUserId().equals(visitorId)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }


}
