package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.projectsList.ProjectListDto;
import com.eskiiimo.api.projects.projectsList.ProjectListResource;
import com.eskiiimo.api.projects.projectsList.ProjectListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/profiles/{user_id}", produces = MediaTypes.HAL_JSON_VALUE)
public class ProfileController {


    @Autowired
    ProfileService ProfileService;

    @Autowired
    ProjectListService projectListService;



    @GetMapping
    public ResponseEntity getProfile(@PathVariable String user_id){
        ProfileDto profileDto = ProfileService.getProfile(user_id);
        ProfileResource profileResource = new ProfileResource(profileDto,user_id);
        profileResource.add(linkTo(ProfileController.class).slash(user_id).withRel("updateProfile"));
        profileResource.add(new Link("/docs/index.html#resources-profile-get").withRel("profile"));
        return ResponseEntity.ok(profileResource);
    }

    @PutMapping
    public ResponseEntity updateProfile(@PathVariable String user_id,@RequestBody ProfileDto updateData){
        ProfileDto profileDto = ProfileService.updateProfile(user_id,updateData);
        ProfileResource profileResource = new ProfileResource(profileDto,user_id);
        profileResource.add(new Link("/docs/index.html#resources-profile-update").withRel("profile"));
        return ResponseEntity.ok(profileResource);
    }

//     //사용자가 참여 중인 프로젝트 리스트 가져오기
//    @GetMapping("/running")
//    public ResponseEntity  getRunningProjects(@PathVariable(value = "user_id") Long user_id,
//                                                   Pageable pageable, PagedResourcesAssembler<Project> assembler) {
//        Page<Project> page = this.projectListService.getRunning(user_id);
//        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
//        pagedResources.add(new Link("/api/projects").withRel("project-list"));
//        pagedResources.add(new Link("/docs/index.html#resources-project-list").withRel("profile"));
//
//        return ResponseEntity.ok(pagedResources);
//
//
//    }
//     //사용자가 참여했던 프로젝트 리스트 가져오기
//    @GetMapping("/ended")
//    public ResponseEntity  getEndedProjects(@PathVariable(value = "user_id") Long user_id,
//                                            Pageable pageable, PagedResourcesAssembler<Project> assembler) {
//        Page<Project> page = this.projectListService.getEnded(user_id);
//        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
//        pagedResources.add(new Link("/api/projects").withRel("project-list"));
//        pagedResources.add(new Link("/docs/index.html#resources-project-list").withRel("profile"));
//
//        return ResponseEntity.ok(pagedResources);
//
//    }
//
//    // 사용자가 기획한 프로젝트 리스트 가져오기
//    @GetMapping("/leader")
//    public ResponseEntity  getMyPlanProjects(@PathVariable(value = "user_id") Long user_id,
//                                             Pageable pageable, PagedResourcesAssembler<Project> assembler) {
//        Page<Project> page = this.projectListService.getLeader(user_id);
//        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
//        pagedResources.add(new Link("/api/projects").withRel("project-list"));
//        pagedResources.add(new Link("/docs/index.html#resources-project-list").withRel("profile"));
//
//        return ResponseEntity.ok(pagedResources);
//
//    }
}
