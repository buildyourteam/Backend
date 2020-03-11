package com.eskiiimo.api.projects.detail;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectDetailResource extends EntityModel<ProjectDetailDto> {

    public ProjectDetailResource(ProjectDetailDto project, Long project_id,Link... links) {
        super(project, links);
        add(linkTo(ProjectDetailController.class).slash(project_id).withSelfRel());
    }
}
