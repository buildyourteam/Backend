package com.eskiiimo.api.projects.projectdetail;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectMemberResource extends EntityModel<ProjectMemberDto> {

    public ProjectMemberResource(ProjectMemberDto projectMember, String user_id, Link... links) {
        super(projectMember, links);
        add(new Link("/api/profile/"+user_id).withSelfRel());
    }
}
