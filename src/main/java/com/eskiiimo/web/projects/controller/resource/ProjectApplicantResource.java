package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.repository.projects.dto.ProjectApplicantDto;
import com.eskiiimo.web.projects.controller.ProjectApplyController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectApplicantResource extends EntityModel<ProjectApplicantDto> {

    public ProjectApplicantResource(ProjectApplicantDto applicant, Long projectId,Link... links) {
        super(applicant, links);
        add(linkTo(ProjectApplyController.class,projectId).slash(applicant.getUserId()).withSelfRel());
    }

}
