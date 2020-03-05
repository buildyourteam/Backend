package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApply;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
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

    @Transactional
    public void addLeader(Project project, String userId){
        Optional<User> optionalUser = this.userRepository.findByUserId(userId);
        User user = optionalUser.get();
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.LEADER)
                .user(user)
                .project(project)
                .build();
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }
    @Transactional
    public boolean applyProject(Long projectId, ProjectApplyDto apply, String visitorId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isEmpty())
            return Boolean.FALSE;
        Project project = optionalProject.get();
        Optional<User> optionalUser = userRepository.findByUserId(visitorId);
        if(optionalUser.isEmpty())
            return Boolean.FALSE;
        ProjectApply projectApplyEntity = apply.toEntity(optionalUser.get());
        project.getApplies().add(projectApplyEntity);
        this.projectApplyRepository.save(projectApplyEntity);
        this.projectRepository.save(project);
        return Boolean.TRUE;
    }
    public boolean updateApply(Long projectId, ProjectApplyDto apply, String visitorId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isEmpty())
            return Boolean.FALSE;
        Project project = optionalProject.get();
        Optional<ProjectApply> optionalApply = projectApplyRepository.findByUser_UserId(visitorId);
        ProjectApply projectApply = optionalApply.get();
        project.getApplies().remove(projectApply);
        projectApply.setSelfDescription(apply.getSelfDescription());
        projectApply.setAnswers(apply.getAnswers());
        project.getApplies().add(projectApply);
        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
        return Boolean.TRUE;
    }
    @Transactional
    public List<ProjectApplicantDto> getApplicants(Long projectId, String visitorId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isEmpty())
            return null;
        Project project =  optionalProject.get();
        if(this.isLeader(project,visitorId)){
            List<ProjectApplicantDto> applicants = new ArrayList<ProjectApplicantDto>();
            for(ProjectApply projectApply : project.getApplies()){
                ProjectApplicantDto projectApplicantDto =ProjectApplicantDto.builder()
                        .status(projectApply.getStatus())
                        .userId(projectApply.getUser().getUserId())
                        .userName(projectApply.getUser().getUsername())
                        .role(projectApply.getRole())
                        .build();
                applicants.add(projectApplicantDto);
            }
            return applicants;
        }
        else
            return null;
    }
    @Transactional
    public ProjectApplyDto getApply(Long projectId, String userId, String visitorId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isEmpty())
            return null;
        Project project =  optionalProject.get();
        if(this.isLeader(project,visitorId)) {
            for(ProjectApply projectApply : project.getApplies()){
                if(projectApply.getUser().getUserId().equals(userId)){
                    projectApply.setStatus(ProjectApplyStatus.READ);
                    projectApplyRepository.save(projectApply);
                    ProjectApplyDto projectApplyDto = ProjectApplyDto.builder()
                            .userName(projectApply.getUser().getUsername())
                            .questions(project.getProjectApplyQuestions())
                            .answers(projectApply.getAnswers())
                            .selfDescription(projectApply.getSelfDescription())
                            .canUploadFile(projectApply.getCanUploadFile())
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
    public Boolean acceptApply(Long projectId, String userId, String visitorId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if(optionalProject.isEmpty())
            return null;
        Project project =  optionalProject.get();
        if(isLeader(project,visitorId)){
            ProjectRole memberRole=null;
            for(ProjectApply projectApply : project.getApplies()){
                if(projectApply.getUser().getUserId().equals(userId)){
                    memberRole= projectApply.getRole();
                    projectApply.setStatus(ProjectApplyStatus.ACCEPT);
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
            this.projectMemberRepository.save(projectMember);
            this.projectRepository.save(project);
            return Boolean.TRUE;
        }
        else
            return Boolean.FALSE;
    }
    @Transactional
    public Boolean rejectApply(Long projectId, String userId, String visitorId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isEmpty())
            return null;
        Project project = optionalProject.get();
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
