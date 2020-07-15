package com.eskiiimo.web.user.controller.resource;

import com.eskiiimo.repository.user.dto.PeopleDto;
import com.eskiiimo.web.files.controller.ProfileImageController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PeopleResource extends EntityModel<PeopleDto> {

    public PeopleResource(PeopleDto people, Link... links) {
        super(people, links);
        add(new Link("/profile/"+ people.getUserId()).withSelfRel());
        add(linkTo(ProfileImageController.class,people.getUserId()).withRel("profileImage"));
    }
}
