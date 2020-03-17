package com.eskiiimo.api.projects.list;

import com.eskiiimo.api.files.projectimage.ProjectImageController;
import com.eskiiimo.api.projects.Project;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectListResource extends EntityModel<Project> {

    public ProjectListResource(Project project, Link... links) {
        super(project, links);
        add(new Link("/projects/"+project.getProjectId()).withSelfRel());
        add(linkTo(ProjectImageController.class,project.getProjectId()).withRel("projectImage"));
    }
}
