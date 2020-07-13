package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.web.projects.controller.ProjectDetailController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class RecruitsResource extends CollectionModel<List<RecruitResource>> {
    public RecruitsResource(List<RecruitResource> recruits, Long project_id, Link... links){
        super(Collections.singleton(recruits), links);
        add(linkTo(ProjectDetailController.class).slash(project_id+"/recruits").withSelfRel());
    }
}