package com.eskiiimo.web.projects.response;

import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.RecruitController;
import com.eskiiimo.web.projects.controller.resource.RecruitResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class GetRecruitsToMeResponse extends CollectionModel<List<RecruitResponse>> {
    public GetRecruitsToMeResponse(List<RecruitResponse> recruits, String userId, Link... links){
        super(Collections.singleton(recruits), links);
        add(linkTo(RecruitController.class, userId).withSelfRel());
        add(linkTo(DocsController.class).slash("#getRecruits").withRel("profile"));

    }
}
