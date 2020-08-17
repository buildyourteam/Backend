package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectApplicantDto;
import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.projects.model.ProjectApplyAnswer;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.exception.*;
import com.eskiiimo.web.projects.request.ProjectApplyRequest;
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
    public void applyProject(Long projectId, ProjectApplyRequest apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        User user = userRepository.findByUserIdAndActivate(visitorId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(visitorId));

        isDuplicateApply(project, user, visitorId);

        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for (String answer : apply.getAnswers())
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());

        ProjectApply projectApply = ProjectApply.builder()
                .answers(answers)
                .introduction(apply.getIntroduction())
                .state(ProjectApplyState.UNREAD)
                .user(user)
                .role(apply.getRole())
                .build();

        project.addApply(projectApply);
    }

    @Transactional
    public void updateApply(Long projectId, ProjectApplyRequest apply, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectApply projectApply = findApply(project, visitorId);
        projectApply.updateApply(apply.getIntroduction(), apply.getRole(), apply.getAnswers());
        project.updateApplies(projectApply);
    }

    @Transactional(readOnly = true)
    public List<ProjectApplicantDto> getApplicants(Long projectId, String visitorId) {
        Project project = getProjectList(projectId, visitorId);

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
                .userName(projectApply.getUser().getUserName())
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

    private Project getProjectList(Long projectId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        if (!this.isLeader(project, visitorId))
            throw new YouAreNotLeaderException(visitorId);

        if (project.getApplies() == null ||
                project.getApplies().isEmpty()) {
            throw new EmptyApplicantListException(projectId);
        }

        return project;
    }

    private Project getProjectForLeader(Long projectId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        if (!this.isLeader(project, visitorId))
            throw new YouAreNotLeaderException(visitorId);

        if (project.getApplies() == null ||
            project.getApplies().isEmpty()) {
            throw new ApplicantNotFoundException();
        }

        return project;
    }

    // 프로젝트 중복 지원 여부 검사
    private void isDuplicateApply(Project project, User user, String visitorId) {

        // 프로젝트에서 리더인 경우
        if(isLeader(project, visitorId)) {
            throw new DuplicateApplicantException(visitorId);
        }

        // 프로젝트에 이미 등록된 사용자인 경우
        for(ProjectMember projectMember : project.getProjectMembers()) {
            if(projectMember.getUser().getUserId().equals(visitorId)) {
                throw new DuplicateApplicantException(visitorId);
            }
        }

        // 프로젝트 지원자가 없는 경우에 대한 예외 처리
        if(project.getApplies() != null) {

            // 프로젝트에 이미 지원한 사용자인 경우
            for (ProjectApply projectApply : project.getApplies()) {
                if (projectApply.getUser().getAccountId() == user.getAccountId()) {
                    throw new DuplicateApplicantException(visitorId);
                }
            }
        }
    }

}
