package com.eskiiimo.web.index;

import com.eskiiimo.web.projects.controller.ProjectDetailController;
import com.eskiiimo.web.projects.controller.ProjectListController;
import com.eskiiimo.web.user.controller.PeopleController;
import com.eskiiimo.web.user.controller.ProfileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value ="/index", produces = MediaTypes.HAL_JSON_VALUE)
public class IndexController {
    @Autowired
    IndexService indexService;

    @GetMapping
    public RepresentationModel mainIndex(){
        long projectListSize=10;
        long peopleSize = 5;
        long randomProject = indexService.getRandomProjectPage(projectListSize);
        long randomPeople = indexService.getRandomPeoplePage(peopleSize);
        var mainIndex = new RepresentationModel();
        mainIndex.add(linkTo(ProjectListController.class)
                .slash("?page="+randomProject+"&size="+projectListSize).withRel("projectList"));
        mainIndex.add(linkTo(ProjectListController.class)
                .slash("deadline?page="+randomProject+"&size="+projectListSize).withRel("projectListDeadline"));
        mainIndex.add(linkTo(PeopleController.class)
                .slash("?page="+randomPeople+"&size="+peopleSize).withRel("peopleList"));
        mainIndex.add(linkTo(ProjectDetailController.class).withRel("createProject"));
        return mainIndex;
    }
    @GetMapping(value = "/projects")
    public RepresentationModel projectsIndex(){
        long projectListSize = 10;
        var projectListIndex = new RepresentationModel();
        projectListIndex.add(linkTo(ProjectListController.class)
                .slash("?page=0&size="+projectListSize).withRel("projectList"));
        projectListIndex.add(linkTo(ProjectDetailController.class).withRel("createProject"));
        return projectListIndex;
    }
    @GetMapping(value = "/projects/{project_id}")
    public RepresentationModel projectDetailIndex(@PathVariable Long project_id){
        var projectIndex = new RepresentationModel();
        projectIndex.add(linkTo(ProjectDetailController.class).slash(project_id).withRel("projectDetail"));
        return projectIndex;
    }
    @GetMapping(value = "/people")
    public RepresentationModel peopleIndex(){
        long peopleSize = 10;
        var peopleIndex = new RepresentationModel();
        peopleIndex.add(linkTo(PeopleController.class)
                .slash("?page=0&size="+peopleSize).withRel("peopleList"));
        return peopleIndex;
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
