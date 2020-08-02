package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectApplicantDto;
import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectApplyRepository;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.exception.ApplicantNotFoundException;
import com.eskiiimo.web.projects.exception.ApplyNotFoundException;
import com.eskiiimo.web.projects.exception.ProjectNotFoundException;
import com.eskiiimo.web.projects.exception.YouAreNotLeaderException;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.exception.UserNotFoundException;
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

    @Transactional
    public void addLeader(Project project, String userId) {
        User user = userRepository.findByUserIdAndActivate(userId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));

        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.LEADER)
                .user(user)
                .project(project)
                .hide(Boolean.FALSE)
                .build();
        projectMember.joinProject(project);

        this.projectMemberRepository.save(projectMember);
    }

    @Transactional
    public void applyProject(Long projectId, ProjectApplyDto apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        User user = userRepository.findByUserIdAndActivate(visitorId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(visitorId));

        ProjectApply projectApply = apply.toEntity(user);
        project.addApply(projectApply);
    }

    @Transactional
    public void updateApply(Long projectId, ProjectApplyDto apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectApply projectApply = findApply(project, visitorId);
        projectApply.updateApply(apply.getIntroduction(), apply.getRole(), apply.getAnswers());
        project.updateApplies(projectApply);
    }

    @Transactional(readOnly = true)
    public List<ProjectApplicantDto> getApplicants(Long projectId, String visitorId) {
        Project project = getProjectForLeader(projectId, visitorId);

        List<ProjectApplicantDto> applicants = new ArrayList<ProjectApplicantDto>();
        for (ProjectApply projectApply : project.getApplies()) {
            ProjectApplicantDto projectApplicantDto = ProjectApplicantDto.builder()
                    .state(projectApply.getState())
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
        Project project;
        if (userId.equals(visitorId))
            project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new ProjectNotFoundException(projectId));
        else
            project = getProjectForLeader(projectId, visitorId);

        ProjectApply projectApply = findApply(project, userId);
        projectApply.markAsRead();

        return ProjectApplyDto.builder()
                .userName(projectApply.getUser().getUsername())
                .questions(project.getQuestions())
                .answers(projectApply.getAnswers())
                .introduction(projectApply.getIntroduction())
                .state(projectApply.getState())
                .role(projectApply.getRole())
                .build();
    }

    @Transactional
    public void acceptApply(Long projectId, String userId, String visitorId) {
        Project project = getProjectForLeader(projectId, visitorId);

        ProjectApply projectApply = findApply(project, userId);

        projectApply.setApplyState(ProjectApplyState.ACCEPT);
        project.updateApplies(projectApply);

        ProjectMember projectMember = ProjectMember.builder()
                .role(projectApply.getRole())
                .user(projectApply.getUser())
                .project(project)
                .hide(Boolean.FALSE)
                .introduction(projectApply.getIntroduction())
                .build();
        projectMember.joinProject(project);

        this.projectMemberRepository.save(projectMember);
    }

    @Transactional
    public void rejectApply(Long projectId, String userId, String visitorId) {
        Project project = getProjectForLeader(projectId, visitorId);

        ProjectApply projectApply = findApply(project, userId);
        projectApply.setApplyState(ProjectApplyState.REJECT);
    }

    public Boolean isLeader(Project project, String visitorId) {
        for (ProjectMember projectMember : project.getProjectMembers())
            if (projectMember.getRole().equals(ProjectRole.LEADER))
                if (projectMember.getUser().getUserId().equals(visitorId))
                    return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private ProjectApply findApply(Project project, String userId) {
        for (ProjectApply projectApply : project.getApplies())
            if (projectApply.getUser().getUserId().equals(userId))
                return projectApply;
        throw new ApplyNotFoundException(userId);
    }

    private Project getProjectForLeader(Long projectId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        if (!this.isLeader(project, visitorId))
            throw new YouAreNotLeaderException(visitorId);
        if (project.getApplies() == null)
            throw new ApplicantNotFoundException(projectId);
        return project;
    }
}
