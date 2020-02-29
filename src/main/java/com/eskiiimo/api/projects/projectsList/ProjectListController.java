package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectField;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        if (page ==null) {
            return ResponseEntity.notFound().build();
        }
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(linkTo(ProjectListController.class).withRel("project-list"));
        pagedResources.add(linkTo(DocsController.class).slash("#resourcesProjectList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/deadline")
    public ResponseEntity getProjectsDeadline(Pageable pageable, PagedResourcesAssembler<Project> assembler) {

        Page<Project> page = projectListService.findAllByDdayLessThanOrderByDdayAsc(pageable);
        if (page == null) {
            return ResponseEntity.notFound().build();
        }
        PagedModel<ProjectListResource> pagedResources = assembler.toModel(page, e -> new ProjectListResource(e));
        pagedResources.add(linkTo(ProjectListController.class).slash("/deadline").withRel("deadline-project-list"));
        pagedResources.add(linkTo(DocsController.class).slash("#resourcesDeadlineProjectList").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }





}
