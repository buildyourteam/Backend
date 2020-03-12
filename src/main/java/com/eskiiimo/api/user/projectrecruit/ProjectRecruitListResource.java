package com.eskiiimo.api.user.projectrecruit;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class ProjectRecruitListResource extends CollectionModel<List<ProjectRecruitResource>> {
    public ProjectRecruitListResource(List<ProjectRecruitResource> recruits, String userId, Link... links){
        super(Collections.singleton(recruits), links);
        add(linkTo(ProjectRecruitController.class, userId).withSelfRel());
    }
}
