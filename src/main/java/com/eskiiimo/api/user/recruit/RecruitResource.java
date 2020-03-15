package com.eskiiimo.api.user.recruit;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class RecruitResource extends EntityModel<RecruitDto> {
    public RecruitResource(RecruitDto recruits, String userId, Link... links){
        super(recruits, links);
        add(linkTo(RecruitController.class, userId).slash(recruits.getProjectId()).withSelfRel());
    }
}
