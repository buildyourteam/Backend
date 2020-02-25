package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectField;
import com.eskiiimo.api.projects.projectdetail.ProjectDetailService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
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
@RequestMapping(value = "/projects", produces = MediaTypes.HAL_JSON_VALUE)
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
        projectListResource.add(linkTo(ProjectListController.class).withRel("create-project"));
        projectListResource.add(linkTo(DocsController.class).slash("#resourcesProjectCreate").withRel("profile"));

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
        pagedResources.add(linkTo(ProjectListController.class).withRel("project-list"));
        pagedResources.add(linkTo(DocsController.class).slash("#resourcesProjectList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/deadline")
    public ResponseEntity getProjectsDeadline(Pageable pageable, PagedResourcesAssembler<Project> assembler) {

        Page<Project> page = projectListService.findAllByDdayLessThanOrderByDdayAsc(pageable);
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(linkTo(ProjectListController.class).slash("/deadline").withRel("deadline-project-list"));
        pagedResources.add(linkTo(DocsController.class).slash("#resourcesDeadlineProjectList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @PutMapping("/{project_id}")
    public ResponseEntity updateProject(@PathVariable Long project_id,
                                        @RequestBody ProjectListDto projectListDto,
                                        Errors errors) {
        Project project = projectListService.updateProject(project_id, projectListDto);
        ProjectListResource projectListResource = new ProjectListResource(project);
        projectListResource.add(linkTo(DocsController.class).slash("#resourcesProjectUpdate").withRel("profile"));
        return ResponseEntity.ok(projectListResource);
    }

    @DeleteMapping("/{project_id}")
    public ResponseEntity deleteProject(@PathVariable Long project_id) {
        this.projectListService.deleteProject(project_id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }



}
