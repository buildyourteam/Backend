package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.projectsList.ProjectListController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectApplicantsResource extends EntityModel<List<ProjectApplicantResource>> {

    public ProjectApplicantsResource(List<ProjectApplicantResource> applicant,Long projectId, Link... links) {
        super(applicant, links);
        add(linkTo(ProjectListController.class).slash(projectId+"/apply").withRel("self"));

    }
}
