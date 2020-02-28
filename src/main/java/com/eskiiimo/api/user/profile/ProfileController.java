package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.projectsList.ProjectListResource;
import com.eskiiimo.api.projects.projectsList.ProjectListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity getProfile(@PathVariable String user_id){
        ProfileDto profileDto = profileService.getProfile(user_id);
        if(profileDto == null)
            return ResponseEntity.notFound().build();
        ProfileResource profileResource = new ProfileResource(profileDto,user_id);
        profileResource.add(linkTo(ProfileController.class).slash(user_id).withRel("updateProfile"));
        profileResource.add(linkTo(DocsController.class).slash("#resourcesProfileGet").withRel("profile"));
        return ResponseEntity.ok(profileResource);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity updateProfile(@PathVariable String user_id,@RequestBody ProfileDto updateData){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null) {
            String userId = authentication.getName();
            System.out.println(userId);
                if(!userId.equals(user_id))
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            ProfileDto profileDto = profileService.updateProfile(user_id,updateData);
            ProfileResource profileResource = new ProfileResource(profileDto, user_id);
            profileResource.add(linkTo(DocsController.class).slash("#resourcesProfileUpdate").withRel("profile"));
            return ResponseEntity.ok(profileResource);
        }
        else {
            System.out.println("noauth");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

     //사용자가 참여 중인 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/running")
    public ResponseEntity  getRunningProjects(@PathVariable(value = "user_id") String user_id,
                                                   Pageable pageable, PagedResourcesAssembler<Project> assembler) {
        Page<Project> page = this.profileService.getRunning(user_id, pageable);
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(linkTo(DocsController.class).slash("#resourcesRunningProjectList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }
     //사용자가 참여했던 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/ended")
    public ResponseEntity  getEndedProjects(@PathVariable(value = "user_id") String user_id,
                                            Pageable pageable, PagedResourcesAssembler<Project> assembler) {
        Page<Project> page = this.profileService.getEnded(user_id, pageable);
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(linkTo(DocsController.class).slash("#resourcesEndedProjectList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    // 사용자가 기획한 프로젝트 리스트 가져오기
    @GetMapping("/{user_id}/plan")
    public ResponseEntity  getMyPlanProjects(@PathVariable(value = "user_id") String user_id,
                                             Pageable pageable, PagedResourcesAssembler<Project> assembler) {
        Page<Project> page = this.profileService.getPlanner(user_id, pageable);
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(linkTo(DocsController.class).slash("#resourcesPlannedProjectList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }
}
