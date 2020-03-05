package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.projects.projectdetail.ProjectDetailController;
import com.eskiiimo.api.projects.projectdetail.ProjectDetailDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectApplicantResource extends EntityModel<ProjectApplicantDto> {

    public ProjectApplicantResource(ProjectApplicantDto applicant, Link... links) {
        super(applicant, links);
        add(linkTo(ProjectApplyController.class).slash(applicant.getUserId()).withSelfRel());
    }
}
