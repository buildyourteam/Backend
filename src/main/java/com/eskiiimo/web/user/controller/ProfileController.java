package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.RecruitController;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import com.eskiiimo.web.user.response.ProfileResponse;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/profile", produces = MediaTypes.HAL_JSON_VALUE)
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse getProfile(
            @PathVariable String user_id
    ) {
        ProfileResponse profileResponse = new ProfileResponse(
                profileService.getProfile(user_id),
                user_id);
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user_id.equals(visitorId)) {
            profileResponse.add(linkTo(ProfileController.class).slash(user_id).withRel("updateProfile"));
            profileResponse.add(linkTo(RecruitController.class, user_id).withRel("recruits"));
        }
        profileResponse.add(linkTo(DocsController.class).slash("#resourcesProfileGet").withRel("profile"));
        return profileResponse;
    }

    @PutMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponse updateProfile(
            @PathVariable String user_id,
            @RequestBody UpdateProfileRequest updateData
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        ProfileDto profileDto = profileService.updateProfile(user_id, visitorId, updateData);
        ProfileResponse profileResponse = new ProfileResponse(profileDto, user_id);
        profileResponse.add(linkTo(DocsController.class).slash("#resourcesProfileUpdate").withRel("profile"));
        return profileResponse;
    }

    //사용자가 참여 중인 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/running")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getRunningProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return assembler.toModel(this.profileService.getRunning(user_id, pageable));
    }

    //사용자가 참여했던 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/ended")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getEndedProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return assembler.toModel(this.profileService.getEnded(user_id, pageable));
    }

    // 사용자가 기획한 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/plan")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getMyPlanProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return assembler.toModel(this.profileService.getPlanner(user_id, pageable));
    }

    //사용자가 참여 중인 숨겨진 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/running/hidden")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getRunningHiddenProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return assembler.toModel(this.profileService.getHiddenRunning(user_id, visitorId, pageable));
    }

    //사용자가 참여했던 숨겨진 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/ended/hidden")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getEndedHiddenProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return assembler.toModel(this.profileService.getHiddenEnded(user_id, visitorId, pageable));
    }

    // 사용자가 기획한 숨겨진 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/plan/hidden")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<ProjectListDto>> getMyPlanHiddenProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return assembler.toModel(this.profileService.getHiddenPlanner(user_id, visitorId, pageable));
    }

    // 숨긴 프로젝트 살리기
    @PutMapping("/{user_id}/projects/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public Object reShowProject(
            @PathVariable(value = "user_id") String user_id,
            @PathVariable(value = "projectId") Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.profileService.reShowProject(user_id, visitorId, projectId);
        return null;
    }

    // 프로젝트 숨기기
    @DeleteMapping("/{user_id}/projects/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public Object hideProject(
            @PathVariable(value = "user_id") String user_id,
            @PathVariable(value = "projectId") Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.profileService.hideProject(user_id, visitorId, projectId);
        return null;
    }
}
