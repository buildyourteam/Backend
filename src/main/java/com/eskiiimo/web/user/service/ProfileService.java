package com.eskiiimo.web.user.service;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.common.exception.ProjectNotFoundException;
import com.eskiiimo.web.common.exception.UserNotFoundException;
import com.eskiiimo.web.projects.enumtype.State;
import com.eskiiimo.web.user.exception.NotYourProfileException;
import com.eskiiimo.web.user.exception.YouAreNotMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Transactional
    public ProfileDto getProfile(String user_id) {
        User profile = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProfileDto profileDto = profile.toProfileDto();
        return profileDto;
    }

    @Transactional
    public ProfileDto updateProfile(String user_id, String visitorId, ProfileDto updateData) {
        if (!user_id.equals(visitorId))
            throw new NotYourProfileException("프로필 수정 권한이 없습니다.");
        User profile = userRepository.findByUserId(user_id)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        updateData.updateProfile(profile);
        return this.userRepository.save(profile).toProfileDto();
    }

    public Page<ProjectListDto> getRunning(String user_id, Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(user_id, Boolean.FALSE, State.RUNNING, pageable);
        return page;
    }

    public Page<ProjectListDto> getEnded(String user_id, Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(user_id, Boolean.FALSE, State.ENDED, pageable);
        return page;
    }

    public Page<ProjectListDto> getPlanner(String user_id, Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(user_id, Boolean.FALSE, pageable);
        return page;
    }

    public Page<ProjectListDto> getHiddenRunning(String user_id, String visitorId, Pageable pageable) {
        if (!user_id.equals(visitorId))
            throw new NotYourProfileException("프로필 수정 권한이 없습니다.");
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(user_id, Boolean.TRUE, State.RUNNING, pageable);
        return page;
    }

    public Page<ProjectListDto> getHiddenEnded(String user_id, String visitorId, Pageable pageable) {
        if (!user_id.equals(visitorId))
            throw new NotYourProfileException("프로필 수정 권한이 없습니다.");
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(user_id, Boolean.TRUE, State.ENDED, pageable);
        return page;
    }

    public Page<ProjectListDto> getHiddenPlanner(String user_id, String visitorId, Pageable pageable) {
        if (!user_id.equals(visitorId))
            throw new NotYourProfileException("프로필 수정 권한이 없습니다.");
        Page<ProjectListDto> page = this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(user_id, Boolean.TRUE, pageable);
        return page;
    }

    public void reShowProject(String user_id, String visitorId, Long projectId) {
        setProjectVisible(user_id, visitorId, projectId, Boolean.TRUE);
    }


    public void hideProject(String user_id, String visitorId, Long projectId) {
        setProjectVisible(user_id, visitorId, projectId, Boolean.FALSE);
    }

    public void setProjectVisible(String user_id, String visitorId, Long projectId, Boolean visible) {
        if (!user_id.equals(visitorId))
            throw new NotYourProfileException("프로필 수정 권한이 없습니다.");
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        ProjectMember projectMember = this.projectMemberRepository.findByProject_ProjectIdAndUser_UserId(projectId, user_id)
                .orElseThrow(() -> new YouAreNotMemberException("프로젝트에 소속되어있지 않습니다."));
        project.getProjectMembers().remove(projectMember);
        projectMember.setHide(!visible);
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }
}
