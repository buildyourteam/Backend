package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.RecruitState;
import com.eskiiimo.web.projects.exception.*;
import com.eskiiimo.web.projects.request.RecruitProjectRequest;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final RecruitRepository recruitRepository;

    @Transactional
    public void recruitProject(String userId, RecruitProjectRequest recruit, String visitorId) {
        Project project = projectRepository.findById(recruit.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(recruit.getProjectId()));
        if (!this.isLeader(project, visitorId))
            throw new YouAreNotLeaderException(visitorId);
        User user = userRepository.findByUserIdAndActivate(userId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));

        checkDuplicateRecruit(project, userId);

        Recruit projectRecruit = Recruit.builder()
                .role(recruit.getRole())
                .introduction(recruit.getIntroduction())
                .user(user)
                .project(project)
                .state(RecruitState.UNREAD)
                .build();

        this.recruitRepository.save(projectRecruit);
    }

    @Transactional(readOnly = true)
    public List<RecruitDto> getRecruitList(String userId, String visitorId) {
        if (!userId.equals(visitorId))
            throw new RecruitNotAuthException();

        List<Recruit> RecruitList = this.recruitRepository.findAllByUser_UserId(visitorId)
                .orElseThrow(() -> new EmptyRecruitListException(userId));

        List<RecruitDto> projectRecruits = new ArrayList<RecruitDto>();
        for (Recruit recruit : RecruitList) {
            RecruitDto recruitDto = RecruitDto.builder()
                    .introduction(recruit.getIntroduction())
                    .projectId(recruit.getProject().getProjectId())
                    .projectName(recruit.getProject().getProjectName())
                    .role(recruit.getRole())
                    .state(recruit.getState())
                    .userName(recruit.getUser().getUserName())
                    .build();
            projectRecruits.add(recruitDto);
        }
        return projectRecruits;
    }

    @Transactional
    public RecruitDto getRecruit(String userId, Long projectId, String visitorId) {
        Recruit recruit = getRecruitToMe(userId, projectId, visitorId);
        recruit.markAsRead();
        return RecruitDto.builder()
                .introduction(recruit.getIntroduction())
                .projectId(recruit.getProject().getProjectId())
                .projectName(recruit.getProject().getProjectName())
                .role(recruit.getRole())
                .state(recruit.getState())
                .userName(recruit.getUser().getUserName())
                .build();
    }

    @Transactional
    public void acceptRecruit(String userId, Long projectId, String visitorId) {
        Recruit recruit = getRecruitToMe(userId, projectId, visitorId);
        recruit.setRecruitState(RecruitState.ACCEPT);
        this.recruitRepository.save(recruit);
    }

    @Transactional
    public void rejectRecruit(String userId, Long projectId, String visitorId) {
        Recruit recruit = getRecruitToMe(userId, projectId, visitorId);
        recruit.setRecruitState(RecruitState.REJECT);
        this.recruitRepository.save(recruit);
    }

    private Recruit getRecruitToMe(String userId, Long projectId, String visitorId) {
        if (!userId.equals(visitorId))
            throw new RecruitNotAuthException();
        return this.recruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId)
                .orElseThrow(() -> new RecruitNotFoundException());
    }

    public Boolean isLeader(Project project, String visitorId) {
        for (ProjectMember projectMember : project.getProjectMembers())
            if (projectMember.getRole().equals(ProjectRole.LEADER))
                if (projectMember.getUser().getUserId().equals(visitorId))
                    return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private void checkDuplicateRecruit(Project project, String userId) {
        // 팀원 여부 체크
        for (ProjectMember projectMember : project.getProjectMembers()) {
            if (projectMember.getUser().getUserId().equals(userId)) {
                throw new DuplicatedRecruitException(userId);
            }
        }

        // 영입제안 중복 체크
        if (recruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, project.getProjectId()).isPresent())
            throw new DuplicatedRecruitException(userId);

    }

}
