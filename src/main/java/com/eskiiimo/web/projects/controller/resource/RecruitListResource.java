package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.web.projects.controller.RecruitController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class RecruitListResource extends CollectionModel<List<RecruitResource>> {
    public RecruitListResource(List<RecruitResource> recruits, String personId, Link... links){
        super(Collections.singleton(recruits), links);
        add(linkTo(RecruitController.class, personId).withSelfRel());
    }
}
