package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.web.projects.controller.ProjectApplyController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectApplyResource extends EntityModel<ProjectApplyDto> {

    public ProjectApplyResource(ProjectApplyDto applicant,Long projectId,String personId, Link... links) {
        super(applicant, links);
        add(linkTo(ProjectApplyController.class,projectId).slash(personId).withSelfRel());
    }
}
