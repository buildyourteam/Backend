package com.eskiiimo.api.projects.projectapply;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectApplyResource extends EntityModel<ProjectApplyDto> {

    public ProjectApplyResource(ProjectApplyDto applicant,Long projectId,String userId, Link... links) {
        super(applicant, links);
        add(linkTo(ProjectApplyController.class,projectId).slash(userId).withSelfRel());
    }
}
