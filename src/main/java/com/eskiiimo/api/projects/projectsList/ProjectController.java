package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping(value="/api/projects", produces= MediaTypes.HAL_JSON_VALUE)
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping
    public ResponseEntity getProjectsList(Pageable pageable, PagedResourcesAssembler<Project> assembler) {

        Page<Project> page = this.projectRepository.findAll(pageable);
        PagedModel<ProjectResource> pagedResources = assembler.toModel(page, e -> new ProjectResource(e));
        pagedResources.add(new Link("/api/projects").withRel("project-list"));
        pagedResources.add(new Link("/html5/index.html#resources-projects-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);

    }
}
