package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.common.ErrorResource;
import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.Project;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectDetailController {

    @Autowired
    ProjectDetailService projectDetailService;

    @Autowired
    ModelMapper modelMapper;


    @GetMapping(value = "/{project_id}")
    public ResponseEntity getProjectDetail(@PathVariable Long project_id){
        ProjectDetailDto projectDetailDto = projectDetailService.getProject(project_id);
        if(projectDetailDto == null)
            return ResponseEntity.notFound().build();
        ProjectDetailResource projectDetailResource = new ProjectDetailResource(projectDetailDto,project_id);
        projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id+"/apply").withRel("apply"));
        projectDetailResource.add(linkTo(DocsController.class).slash("#resourcesProjectGet").withRel("profile"));
        return ResponseEntity.ok(projectDetailResource);
    }

    @PostMapping
    public ResponseEntity createProject(@RequestBody @Valid ProjectDetailDto projectDetailDto, Errors errors) {

        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        /*
        ProjectDetail validator
         */

        Project newProject = this.projectDetailService.storeProject(projectDetailDto);
        ControllerLinkBuilder selfLinkBuilder = linkTo(ProjectDetailController.class).slash(newProject.getProjectId());
        URI createdUri = selfLinkBuilder.toUri();
        ProjectDetailDto projectDetailDto1 = modelMapper.map(newProject, ProjectDetailDto.class);
        ProjectDetailResource projectDetailResource = new ProjectDetailResource(projectDetailDto1, newProject.getProjectId());
        projectDetailResource.add(linkTo(ProjectDetailController.class).withRel("create-project"));
        projectDetailResource.add(linkTo(DocsController.class).slash("#resourcesProjectCreate").withRel("profile"));
        return ResponseEntity.created(createdUri).body(projectDetailResource);
    }

    @PutMapping("/{project_id}")
    public ResponseEntity updateProject(@PathVariable Long project_id,
                                        @RequestBody ProjectDetailDto projectDetailDto,
                                        Errors errors) {
        ProjectDetailDto project = projectDetailService.updateProject(project_id, projectDetailDto);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        ProjectDetailResource projectDetailResource = new ProjectDetailResource(project, project_id);
        projectDetailResource.add(linkTo(DocsController.class).slash("#resourcesProjectUpdate").withRel("profile"));
        return ResponseEntity.ok(projectDetailResource);
    }

    @DeleteMapping("/{project_id}")
    public ResponseEntity deleteProject(@PathVariable Long project_id) {
        this.projectDetailService.deleteProject(project_id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
