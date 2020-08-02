package com.eskiiimo.web.projects.response;

import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.ProjectDetailController;
import com.eskiiimo.web.projects.controller.resource.RecruitResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class GetRecruitsResponse extends CollectionModel<List<RecruitResponse>> {
    public GetRecruitsResponse(List<RecruitResponse> recruits, Long project_id, Link... links){
        super(Collections.singleton(recruits), links);
        add(linkTo(ProjectDetailController.class).slash(project_id+"/recruits").withSelfRel());
        add(linkTo(DocsController.class).slash("#getSendRecruits").withRel("profile"));
    }
}