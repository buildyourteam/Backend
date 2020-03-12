package com.eskiiimo.api.user.projectrecruit;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProjectRecruitResource extends EntityModel<ProjectRecruitDto> {
    public ProjectRecruitResource(ProjectRecruitDto recruits, String userId, Link... links){
        super(recruits, links);
        add(linkTo(ProjectRecruitController.class, userId).slash(recruits.getProjectId()).withSelfRel());
    }
}
