package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectField;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Controller
@Slf4j
@RequestMapping(value = "/api/projects", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectController {


    @Autowired
    ProjectService projectService;

    @Autowired
    ModelMapper modelMapper;

    //  validation작성하기
    @PostMapping
    public ResponseEntity createProject(@RequestBody @Valid ProjectDto projectDto, Errors errors) {
        Project newProject = this.projectService.storeProject(projectDto);
        ControllerLinkBuilder selfLinkBuilder = linkTo(ProjectController.class).slash(newProject.getProjectId());
        URI createdUri = selfLinkBuilder.toUri();
        ProjectResource projectResource = new ProjectResource(newProject);
        projectResource.add(new Link("/api/projects").withRel("create-project"));
        projectResource.add(new Link("/docs/index.html#resources-project-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(projectResource);
    }


    /*
    직군별 검색기능 value="occupation"
    분야별 검색기능 value="field"
     */

    @GetMapping
    public ResponseEntity getProjectsList(Pageable pageable, PagedResourcesAssembler<Project> assembler,
                                          @RequestParam(value = "occupation", required = false) String occupation,
                                          @RequestParam(value = "field", required = false) ProjectField field
    ) {
        Page<Project> page = this.projectService.getAllByField(occupation, field, pageable);
        PagedModel<ProjectResource> pagedResources = assembler.toModel(page, e -> new ProjectResource(e));
        pagedResources.add(new Link("/api/projects").withRel("project-list"));
        pagedResources.add(new Link("/docs/index.html#resources-project-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/deadline")
    public ResponseEntity getProjectsDeadline(Pageable pageable, PagedResourcesAssembler<Project> assembler) {

        Page<Project> page = projectService.findAllByDdayLessThanOrderByDdayAsc(pageable);
        PagedModel<ProjectResource> pagedResources = assembler.toModel(page, e -> new ProjectResource(e));
        pagedResources.add(new Link("/api/projects/deadline").withRel("deadline-project-list"));
        pagedResources.add(new Link("/html5/index.html#resources-deadline-project-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

}
