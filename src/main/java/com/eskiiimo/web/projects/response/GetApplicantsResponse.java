package com.eskiiimo.web.projects.response;

import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.ProjectApplyController;
import com.eskiiimo.web.projects.controller.resource.ProjectApplicantResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class GetApplicantsResponse extends CollectionModel<List<ProjectApplicantResource>> {

    public GetApplicantsResponse(List<ProjectApplicantResource> applicants, Long projectId, Link... links) {
        super(Collections.singleton(applicants), links);
        add(linkTo(ProjectApplyController.class, projectId).withSelfRel());
        add(linkTo(DocsController.class).slash("#getApplicants").withRel("profile"));
    }
}
