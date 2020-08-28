package com.eskiiimo.web.user.service;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.State;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.exception.NotYourProfileException;
import com.eskiiimo.web.user.exception.UserNotFoundException;
import com.eskiiimo.web.user.exception.YouAreNotMemberException;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 프로필 서비스
 *
 * @author always0ne
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final ProjectMemberRepository projectMemberRepository;

    /**
     * 프로필 조회하기
     *
     * @param userId 조회할 사용자의 ID
     * @return {@link ProfileDto}
     */
    @Transactional(readOnly = true)
    public ProfileDto getProfile(String userId) {
        User profile = userRepository.findByUserIdAndActivate(userId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return new ProfileDto(profile);
    }

    /**
     * 프로필 수정하기
     *
     * @param userId               조회할 사용자의 ID
     * @param visitorId            방문자의 ID
     * @param updateProfileRequest {@link UpdateProfileRequest}
     * @return {@link ProfileDto}
     */
    @Transactional
    public ProfileDto updateProfile(String userId, String visitorId, UpdateProfileRequest updateProfileRequest) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        User profile = userRepository.findByUserIdAndActivate(userId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));
        profile.updateProfile(updateProfileRequest.toProfileDto());

        return new ProfileDto(profile);
    }

    /**
     * 사용자가 참여중인 프로젝트 리스트 조회
     *
     * @param userId   조회할 사용자의 ID
     * @param pageable 페이지 정보
     * @return 페이징된 {@link ProjectListDto} 리스트
     */
    @Transactional(readOnly = true)
    public Page<ProjectListDto> getRunning(String userId, Pageable pageable) {
        return this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.FALSE, State.RUNNING, pageable);
    }

    /**
     * 사용자가 참여했던 프로젝트 리스트 조회
     *
     * @param userId   조회할 사용자의 ID
     * @param pageable 페이지 정보
     * @return 페이징된 {@link ProjectListDto} 리스트
     */
    @Transactional(readOnly = true)
    public Page<ProjectListDto> getEnded(String userId, Pageable pageable) {
        return this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.FALSE, State.ENDED, pageable);
    }

    /**
     * 사용자가 기획한 프로젝트 리스트 조회
     *
     * @param userId   조회할 사용자의 ID
     * @param pageable 페이지 정보
     * @return 페이징된 {@link ProjectListDto} 리스트
     */
    @Transactional(readOnly = true)
    public Page<ProjectListDto> getPlanner(String userId, Pageable pageable) {
        return this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(userId, Boolean.FALSE, pageable);
    }

    /**
     * 사용자가 참여중인 숨겨진 프로젝트 리스트 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param visitorId 방문자의 ID
     * @param pageable  페이지 정보
     * @return 페이징된 {@link ProjectListDto} 리스트
     */
    @Transactional(readOnly = true)
    public Page<ProjectListDto> getHiddenRunning(String userId, String visitorId, Pageable pageable) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        return this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.TRUE, State.RUNNING, pageable);
    }

    /**
     * 사용자가 참여했던 숨겨진 프로젝트 리스트 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param visitorId 방문자의 ID
     * @param pageable  페이지 정보
     * @return 페이징된 {@link ProjectListDto} 리스트
     */
    @Transactional(readOnly = true)
    public Page<ProjectListDto> getHiddenEnded(String userId, String visitorId, Pageable pageable) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        return this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(userId, Boolean.TRUE, State.ENDED, pageable);
    }

    /**
     * 사용자가 기획한 숨겨진 프로젝트 리스트 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param visitorId 방문자의 ID
     * @param pageable  페이지 정보
     * @return 페이징된 {@link ProjectListDto} 리스트
     */
    @Transactional(readOnly = true)
    public Page<ProjectListDto> getHiddenPlanner(String userId, String visitorId, Pageable pageable) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        return this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(userId, Boolean.TRUE, pageable);
    }

    /**
     * 사용자가 참여중인 프로젝트 리스트 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param visitorId 방문자의 ID
     * @param projectId 숨김을 취소할 프로젝트의 ID
     */
    @Transactional
    public void reShowProject(String userId, String visitorId, Long projectId) {
        setProjectVisible(userId, visitorId, projectId, Boolean.TRUE);
    }

    /**
     * 사용자가 참여중인 프로젝트 리스트 조회
     *
     * @param userId    조회할 사용자의 ID
     * @param visitorId 방문자의 ID
     * @param projectId 숨김을 취소할 프로젝트의 ID
     */
    @Transactional
    public void hideProject(String userId, String visitorId, Long projectId) {
        setProjectVisible(userId, visitorId, projectId, Boolean.FALSE);
    }

    /**
     * 프로젝트 숨김/취소
     *
     * @param userId    조회할 사용자의 ID
     * @param visitorId 방문자의 ID
     * @param projectId 숨김을 취소할 프로젝트의 ID
     * @param visible   숨기는지 여부
     */
    @Transactional
    void setProjectVisible(String userId, String visitorId, Long projectId, Boolean visible) {
        if (!userId.equals(visitorId))
            throw new NotYourProfileException(userId);
        ProjectMember projectMember = this.projectMemberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new YouAreNotMemberException(projectId));
        projectMember.setVisible(visible);
    }
}
