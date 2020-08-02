package com.eskiiimo.web.projects.response;

import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.ProjectApplyController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectApplyResponse extends EntityModel<ProjectApplyDto> {

    public ProjectApplyResponse(ProjectApplyDto applicant, Long projectId, String userId, Link... links) {
        super(applicant, links);
        add(linkTo(ProjectApplyController.class,projectId).slash(userId).withSelfRel());
        add(linkTo(DocsController.class).slash("#getApply").withRel("profile"));
    }
}
