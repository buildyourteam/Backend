package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.web.projects.controller.RecruitController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class RecruitResponse extends EntityModel<RecruitDto> {
    
    public RecruitResponse(RecruitDto recruits, String userId, Link... links) {
        super(recruits, links);
        add(linkTo(RecruitController.class, userId).slash(recruits.getProjectId()).withSelfRel());
    }
}
