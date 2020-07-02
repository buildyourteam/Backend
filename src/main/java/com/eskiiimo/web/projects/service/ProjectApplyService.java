package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectApplicantDto;
import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.common.exception.*;
import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.projects.model.ProjectApplyAnswer;
import com.eskiiimo.repository.projects.repository.ProjectApplyRepository;
import com.eskiiimo.web.projects.enumtype.ProjectApplyStatus;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.exception.ApplicantNotFoundException;
import com.eskiiimo.web.projects.exception.ApplyNotFoundException;
import com.eskiiimo.web.projects.exception.YouAreNotReaderException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
                .hide(Boolean.FALSE)
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
        if(applicants.isEmpty())
            throw new ApplicantNotFoundException("지원자가 없습니다.");
        return applicants;
    }
    @Transactional
    public ProjectApplyDto getApply(Long projectId, String userId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(this.isLeader(project,visitorId)) {
            for(ProjectApply projectApply : project.getApplies()){
                if(projectApply.getUser().getUserId().equals(userId)){
                    if(projectApply.getStatus()== ProjectApplyStatus.UNREAD)
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
            User user =  userRepository.findByUserId(userId)
                    .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
            ProjectMember projectMember = ProjectMember.builder()
                    .role(memberRole)
                    .user(user)
                    .project(project)
                    .hide(Boolean.FALSE)
                    .build();
            project.getProjectMembers().add(projectMember);
            if(projectMember.getRole()==ProjectRole.DEVELOPER)
                project.getCurrentMember().setDeveloper(project.getCurrentMember().getDeveloper()+1);
            else if(projectMember.getRole()==ProjectRole.DESIGNER)
                project.getCurrentMember().setDesigner(project.getCurrentMember().getDesigner()+1);
            else if(projectMember.getRole()==ProjectRole.PLANNER)
                project.getCurrentMember().setPlanner(project.getCurrentMember().getPlanner()+1);
            else if(projectMember.getRole()==ProjectRole.ETC)
                project.getCurrentMember().setEtc(project.getCurrentMember().getEtc()+1);
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
