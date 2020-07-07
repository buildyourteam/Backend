package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.web.projects.controller.RecruitController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class RecruitResource extends EntityModel<RecruitDto> {
    public RecruitResource(RecruitDto recruits, String personId, Link... links){
        super(recruits, links);
        add(linkTo(RecruitController.class, personId).slash(recruits.getProjectId()).withSelfRel());
    }
}
