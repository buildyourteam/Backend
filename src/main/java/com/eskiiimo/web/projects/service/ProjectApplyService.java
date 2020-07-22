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
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
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

    @Transactional
    public void addLeader(Project project, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

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
    public void applyProject(Long projectId, ProjectApplyDto apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        User user = userRepository.findByUserId(visitorId)
                .orElseThrow(() -> new UserNotFoundException(visitorId));

        ProjectApply projectApply = apply.toEntity(user);
        project.getApplies().add(projectApply);

        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
    }

    @Transactional
    public void updateApply(Long projectId, ProjectApplyDto apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectApply projectApply = findApply(project, visitorId);

        project.getApplies().remove(projectApply);
        projectApply.setIntroduction(apply.getIntroduction());
        projectApply.setRole(apply.getRole());

        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for (String answer : apply.getAnswers())
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());
        projectApply.setAnswers(answers);
        project.getApplies().add(projectApply);

        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
    }

    @Transactional
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
        if (projectApply.getState() == ProjectApplyState.UNREAD)
            projectApply.setState(ProjectApplyState.READ);

        projectApplyRepository.save(projectApply);
        projectRepository.save(project);

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
        project.getApplies().remove(projectApply);
        projectApply.setState(ProjectApplyState.ACCEPT);
        project.getApplies().add(projectApply);

        ProjectMember projectMember = ProjectMember.builder()
                .role(projectApply.getRole())
                .user(projectApply.getUser())
                .project(project)
                .hide(Boolean.FALSE)
                .build();
        project.getProjectMembers().add(projectMember);

        if (projectMember.getRole() == ProjectRole.DEVELOPER)
            project.getCurrentMember().setDeveloper(project.getCurrentMember().getDeveloper() + 1);
        else if (projectMember.getRole() == ProjectRole.DESIGNER)
            project.getCurrentMember().setDesigner(project.getCurrentMember().getDesigner() + 1);
        else if (projectMember.getRole() == ProjectRole.PLANNER)
            project.getCurrentMember().setPlanner(project.getCurrentMember().getPlanner() + 1);
        else if (projectMember.getRole() == ProjectRole.ETC)
            project.getCurrentMember().setEtc(project.getCurrentMember().getEtc() + 1);

        this.projectApplyRepository.save(projectApply);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }

    @Transactional
    public void rejectApply(Long projectId, String userId, String visitorId) {
        Project project = getProjectForLeader(projectId, visitorId);

        ProjectApply projectApply = findApply(project, userId);
        projectApply.setState(ProjectApplyState.REJECT);

        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
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
            throw new YouAreNotReaderException(projectId);
        if (project.getApplies().isEmpty())
            throw new ApplicantNotFoundException(projectId);
        return project;
    }
}
