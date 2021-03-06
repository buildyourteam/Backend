package com.eskiiimo.web.projects.response;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.web.files.controller.ProjectImageController;
import com.eskiiimo.web.projects.controller.ProjectDetailController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectDetailResponse extends EntityModel<ProjectDetailDto> {

    public ProjectDetailResponse(ProjectDetailDto project, Long project_id, Link... links) {
        super(project, links);
        add(linkTo(ProjectDetailController.class).slash(project_id).withSelfRel());
    }
}
