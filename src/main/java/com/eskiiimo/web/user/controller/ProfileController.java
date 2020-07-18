package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.RecruitController;
import com.eskiiimo.web.projects.service.ProjectListService;
import com.eskiiimo.web.user.controller.resource.ProfileResource;
import com.eskiiimo.web.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/profile", produces = MediaTypes.HAL_JSON_VALUE)
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    ProjectListService projectListService;

    @GetMapping("/{user_id}")
    public ResponseEntity getProfile(
            @PathVariable String user_id
    ) {
        ProfileResource profileResource = new ProfileResource(
                profileService.getProfile(user_id),
                user_id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (user_id.equals(authentication.getName())) {
                profileResource.add(linkTo(ProfileController.class).slash(user_id).withRel("updateProfile"));
                profileResource.add(linkTo(RecruitController.class, user_id).withRel("recruits"));
            }
        }
        profileResource.add(linkTo(DocsController.class).slash("#resourcesProfileGet").withRel("profile"));
        return ResponseEntity.ok(profileResource);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity updateProfile(
            @PathVariable String user_id,
            @RequestBody ProfileDto updateData
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        ProfileDto profileDto = profileService.updateProfile(user_id, visitorId, updateData);
        ProfileResource profileResource = new ProfileResource(profileDto, user_id);
        profileResource.add(linkTo(DocsController.class).slash("#resourcesProfileUpdate").withRel("profile"));
        return ResponseEntity.ok(profileResource);
    }

    //사용자가 참여 중인 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/running")
    public ResponseEntity getRunningProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return ResponseEntity.ok(assembler.toModel(this.profileService.getRunning(user_id, pageable)));
    }

    //사용자가 참여했던 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/ended")
    public ResponseEntity getEndedProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return ResponseEntity.ok(assembler.toModel(this.profileService.getEnded(user_id, pageable)));
    }

    // 사용자가 기획한 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/plan")
    public ResponseEntity getMyPlanProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return ResponseEntity.ok(assembler.toModel(this.profileService.getPlanner(user_id, pageable)));
    }

    //사용자가 참여 중인 숨겨진 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/running/hidden")
    public ResponseEntity getRunningHiddenProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(assembler.toModel(this.profileService.getHiddenRunning(user_id, visitorId, pageable)));
    }

    //사용자가 참여했던 숨겨진 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/ended/hidden")
    public ResponseEntity getEndedHiddenProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(assembler.toModel(this.profileService.getHiddenEnded(user_id, visitorId, pageable)));
    }

    // 사용자가 기획한 숨겨진 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/plan/hidden")
    public ResponseEntity getMyPlanHiddenProjects(
            @PathVariable(value = "user_id") String user_id,
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(assembler.toModel(this.profileService.getHiddenPlanner(user_id, visitorId, pageable)));
    }

    // 숨긴 프로젝트 살리기
    @PutMapping("/{user_id}/projects/{projectId}")
    public ResponseEntity reShowProject(
            @PathVariable(value = "user_id") String user_id,
            @PathVariable(value = "projectId") Long projectId,
            Pageable pageable, PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.profileService.reShowProject(user_id, visitorId, projectId);
        return ResponseEntity.ok().build();
    }

    // 프로젝트 숨기기
    @DeleteMapping("/{user_id}/projects/{projectId}")
    public ResponseEntity hideProject(
            @PathVariable(value = "user_id") String user_id,
            @PathVariable(value = "projectId") Long projectId,
            Pageable pageable, PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        this.profileService.hideProject(user_id, visitorId, projectId);
        return ResponseEntity.ok().build();
    }
}
