package com.eskiiimo.api.projects.apply;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectApplicantsResource extends CollectionModel<List<ProjectApplicantResource>> {

    public ProjectApplicantsResource(List<ProjectApplicantResource> applicants,Long projectId, Link... links) {
        super(Collections.singleton(applicants), links);
        add(linkTo(ProjectApplyController.class,projectId).withSelfRel());
    }
}
