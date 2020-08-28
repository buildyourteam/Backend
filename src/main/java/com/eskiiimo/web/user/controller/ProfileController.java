package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import com.eskiiimo.web.user.response.GetProfileResponse;
import com.eskiiimo.web.user.response.UpdateProfileResponse;
import com.eskiiimo.web.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 프로필 컨트롤러
 *
 * @author always0ne
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/profile", produces = MediaTypes.HAL_JSON_VALUE)
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 프로필 조회하기
     *
     * @param userId 조회할 사용자의 ID
     * @return {@link GetProfileResponse}
     * @see ProfileService#getProfile(String) ProfileService.getProfile
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public GetProfileResponse getProfile(
            @PathVariable String userId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        return new GetProfileResponse(profileService.getProfile(userId), userId, visitorId);
    }

    /**
     * 프로필 수정하기
     *
     * @param userId               수정할 사용자의 ID
     * @param updateProfileRequest {@link UpdateProfileRequest}
     * @return {@link UpdateProfileResponse}
     * @see ProfileService#updateProfile(String, String, UpdateProfileRequest) ProfileService.updateProfile
     */
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateProfileResponse updateProfile(
            @PathVariable String userId,
            @RequestBody UpdateProfileRequest updateProfileRequest
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProfileDto profileDto = profileService.updateProfile(userId, visitorId, updateProfileRequest);

        return new UpdateProfileResponse(profileDto, userId);
    }

    /**
     * 사용자가 참여중인 프로젝트 리스트 조회하기
     *
     * @param userId    조회할 사용자의 ID
     * @param pageable  페이징 정보
     * @param assembler page 데이터
     * @return 페이징된 {@link ProjectListDto} 리스트
     * @see ProfileService#getRunning(String, Pageable) ProfileService.getRunning
     */
    @GetMapping("/{userId}/running")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getRunningProjects(
            @PathVariable(value = "userId") String userId,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return assembler.toModel(this.profileService.getRunning(userId, pageable));
    }

    /**
     * 사용자가 참여했던 프로젝트 리스트 조회하기
     *
     * @param userId    조회할 사용자의 ID
     * @param pageable  페이징 정보
     * @param assembler page 데이터
     * @return 페이징된 {@link ProjectListDto} 리스트
     * @see ProfileService#getEnded(String, Pageable) ProfileService.getEnded
     */
    @GetMapping("/{userId}/ended")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getEndedProjects(
            @PathVariable(value = "userId") String userId,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return assembler.toModel(this.profileService.getEnded(userId, pageable));
    }

    /**
     * 사용자가 기획한 프로젝트 리스트 조회하기
     *
     * @param userId    조회할 사용자의 ID
     * @param pageable  페이징 정보
     * @param assembler page 데이터
     * @return 페이징된 {@link ProjectListDto} 리스트
     * @see ProfileService#getPlanner(String, Pageable) ProfileService.getPlanner
     */
    @GetMapping("/{userId}/plan")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getMyPlanProjects(
            @PathVariable(value = "userId") String userId,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return assembler.toModel(this.profileService.getPlanner(userId, pageable));
    }

    /**
     * 사용자가 참여중인 프로젝트 숨겨진 리스트 조회하기
     *
     * @param userId    조회할 사용자의 ID
     * @param pageable  페이징 정보
     * @param assembler page 데이터
     * @return 페이징된 {@link ProjectListDto} 리스트
     * @see ProfileService#getHiddenRunning(String, String, Pageable)  ProfileService.getHiddenRunning
     */
    @GetMapping("/{userId}/running/hidden")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getRunningHiddenProjects(
            @PathVariable(value = "userId") String userId,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return assembler.toModel(this.profileService.getHiddenRunning(userId, visitorId, pageable));
    }

    /**
     * 사용자가 참여했던 숨겨진 프로젝트 리스트 조회하기
     *
     * @param userId    조회할 사용자의 ID
     * @param pageable  페이징 정보
     * @param assembler page 데이터
     * @return 페이징된 {@link ProjectListDto} 리스트
     * @see ProfileService#getHiddenEnded(String, String, Pageable) ProfileService.getHiddenEnded
     */
    @GetMapping("/{userId}/ended/hidden")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getEndedHiddenProjects(
            @PathVariable(value = "userId") String userId,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return assembler.toModel(this.profileService.getHiddenEnded(userId, visitorId, pageable));
    }

    /**
     * 사용자가 기획한 숨겨진 프로젝트 리스트 조회하기
     *
     * @param userId    조회할 사용자의 ID
     * @param pageable  페이징 정보
     * @param assembler page 데이터
     * @return 페이징된 {@link ProjectListDto} 리스트
     * @see ProfileService#getHiddenPlanner(String, String, Pageable) ProfileService.getHiddenPlanner
     */
    @GetMapping("/{userId}/plan/hidden")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getMyPlanHiddenProjects(
            @PathVariable(value = "userId") String userId,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return assembler.toModel(this.profileService.getHiddenPlanner(userId, visitorId, pageable));
    }

    /**
     * 프로젝트 숨기기
     *
     * @param userId    사용자 ID
     * @param projectId 숨길 프로젝트 ID
     * @see ProfileService#hideProject(String, String, Long) ProfileService.hideProject
     */
    @DeleteMapping("/{userId}/projects/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public void hideProject(
            @PathVariable(value = "userId") String userId,
            @PathVariable(value = "projectId") Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.profileService.hideProject(userId, visitorId, projectId);
    }

    /**
     * 사용자가 숨긴 프로젝트 다시 보이기
     *
     * @param userId    사용자 ID
     * @param projectId 다시 보일 프로젝트 ID
     * @see ProfileService#reShowProject(String, String, Long) ProfileService.reShowProject
     */
    @PutMapping("/{userId}/projects/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public void reShowProject(
            @PathVariable(value = "userId") String userId,
            @PathVariable(value = "projectId") Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.profileService.reShowProject(userId, visitorId, projectId);
    }
}
