package com.eskiiimo.api.projects.list;

import com.eskiiimo.api.projects.Project;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class ProjectListResource extends EntityModel<Project> {

    public ProjectListResource(Project project, Link... links) {
        super(project, links);
        add(new Link("/projects/"+project.getProjectId()).withSelfRel());
    }
}
