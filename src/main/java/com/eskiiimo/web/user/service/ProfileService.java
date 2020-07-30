package com.eskiiimo.web.user.service;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.exception.ProjectNotFoundException;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.exception.UserNotFoundException;
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

    @Transactional(readOnly = true)
    public ProfileDto getProfile(String userId) {
        User profile = userRepository.findByUserIdAndActivate(userId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));
        ProfileDto profileDto = profile.toProfileDto();
        return profileDto;
    }

    @Transactional
    public ProfileDto updateProfile(String userId, String visitorId, ProfileDto updateData) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        User profile = userRepository.findByUserIdAndActivate(userId,UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));
        profile.updateProfile(updateData.getUserName(),updateData.getRole(),updateData.getStacks(),updateData.getContact(),updateData.getArea(),updateData.getIntroduction());
        return this.userRepository.save(profile).toProfileDto();
    }

    @Transactional(readOnly = true)
    public Page<ProjectListDto> getRunning(String userId, Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.FALSE, State.RUNNING, pageable);
        return page;
    }

    @Transactional(readOnly = true)
    public Page<ProjectListDto> getEnded(String userId, Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.FALSE, State.ENDED, pageable);
        return page;
    }

    @Transactional(readOnly = true)
    public Page<ProjectListDto> getPlanner(String userId, Pageable pageable) {
        Page<ProjectListDto> page = this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(userId, Boolean.FALSE, pageable);
        return page;
    }

    @Transactional(readOnly = true)
    public Page<ProjectListDto> getHiddenRunning(String userId, String visitorId, Pageable pageable) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.TRUE, State.RUNNING, pageable);
        return page;
    }

    @Transactional(readOnly = true)
    public Page<ProjectListDto> getHiddenEnded(String userId, String visitorId, Pageable pageable) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        Page<ProjectListDto> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.TRUE, State.ENDED, pageable);
        return page;
    }

    @Transactional(readOnly = true)
    public Page<ProjectListDto> getHiddenPlanner(String userId, String visitorId, Pageable pageable) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        Page<ProjectListDto> page = this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(userId, Boolean.TRUE, pageable);
        return page;
    }
    
    @Transactional
    public void reShowProject(String userId, String visitorId, Long projectId) {
        setProjectVisible(userId, visitorId, projectId, Boolean.TRUE);
    }

    @Transactional
    public void hideProject(String userId, String visitorId, Long projectId) {
        setProjectVisible(userId, visitorId, projectId, Boolean.FALSE);
    }

    public void setProjectVisible(String userId, String visitorId, Long projectId, Boolean visible) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectMember projectMember = this.projectMemberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new YouAreNotMemberException(projectId));
        project.getProjectMembers().remove(projectMember);
        projectMember.setVisible(visible);
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }
}
