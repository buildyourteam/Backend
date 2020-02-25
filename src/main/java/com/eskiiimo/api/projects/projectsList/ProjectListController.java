package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectField;
import com.eskiiimo.api.projects.projectdetail.ProjectDetailDto;
import com.eskiiimo.api.projects.projectdetail.ProjectDetailResource;
import com.eskiiimo.api.projects.projectdetail.ProjectDetailService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Controller
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/projects", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectListController {


    @Autowired
    ProjectListService projectListService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProjectDetailService projectDetailService;

    //  validation작성하기
    @PostMapping
    public ResponseEntity createProject(@RequestBody @Valid ProjectListDto projectListDto, Errors errors) {
        Project newProject = this.projectListService.storeProject(projectListDto);
        ControllerLinkBuilder selfLinkBuilder = linkTo(ProjectListController.class).slash(newProject.getProjectId());
        URI createdUri = selfLinkBuilder.toUri();
        ProjectListResource projectListResource = new ProjectListResource(newProject);
        projectListResource.add(new Link("/api/projects").withRel("create-project"));
        projectListResource.add(new Link("/docs/index.html#resources-project-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(projectListResource);
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
        Page<Project> page = this.projectListService.getAllByField(occupation, field, pageable);
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(new Link("/api/projects").withRel("project-list"));
        pagedResources.add(new Link("/docs/index.html#resources-project-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/deadline")
    public ResponseEntity getProjectsDeadline(Pageable pageable, PagedResourcesAssembler<Project> assembler) {

        Page<Project> page = projectListService.findAllByDdayLessThanOrderByDdayAsc(pageable);
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(new Link("/api/projects/deadline").withRel("deadline-project-list"));
        pagedResources.add(new Link("/html5/index.html#resources-deadline-project-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @PutMapping("/{project_id}")
    public ResponseEntity updateProject(@PathVariable Long project_id,
                                        @RequestBody ProjectListDto projectListDto,
                                        Errors errors) {
        Project project = projectListService.updateProject(project_id, projectListDto);
        System.out.println("===================="+project.getProjectName());
        ProjectListResource projectListResource = new ProjectListResource(project);
        projectListResource.add(new Link("/docs/index.html#resources-project-update").withRel("profile"));
        return ResponseEntity.ok(projectListResource);
    }

    @DeleteMapping("/{project_id}")
    public ResponseEntity deleteProject(@PathVariable Long project_id) {
        this.projectListService.deleteProject(project_id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }



}
