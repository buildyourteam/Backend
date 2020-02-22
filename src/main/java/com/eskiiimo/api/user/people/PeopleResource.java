package com.eskiiimo.api.user.people;

import com.eskiiimo.api.user.profile.ProfileController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class PeopleResource extends EntityModel<People> {

    public PeopleResource(People people, Link... links) {
        super(people, links);
        add(linkTo(ProfileController.class).slash(people.getUserId()).withSelfRel());
    }
}
