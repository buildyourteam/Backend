package com.eskiiimo.web.index.controller;

import com.eskiiimo.web.projects.controller.ProjectDetailController;
import com.eskiiimo.web.user.controller.ProfileController;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value ="/index", produces = MediaTypes.HAL_JSON_VALUE)
public class IndexController {

    @GetMapping(value = "/projects/{project_id}")
    public RepresentationModel projectDetailIndex(@PathVariable Long project_id){
        var projectIndex = new RepresentationModel();
        projectIndex.add(linkTo(ProjectDetailController.class).slash(project_id).withRel("projectDetail"));
        return projectIndex;
    }

    @GetMapping(value = "/profile/{user_id}")
    public RepresentationModel profileIndex(@PathVariable String user_id){
        var profileIndex = new RepresentationModel();
        profileIndex.add(linkTo(ProfileController.class).slash(user_id).withRel("profileDetail"));
        profileIndex.add(linkTo(ProfileController.class).slash(user_id+"/running").withRel("runningProjectList"));
        profileIndex.add(linkTo(ProfileController.class).slash(user_id+"/ended").withRel("endedProjectList"));
        profileIndex.add(linkTo(ProfileController.class).slash(user_id+"/plan").withRel("plannedProjectList"));

        return profileIndex;
    }
}
