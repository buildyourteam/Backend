package com.eskiiimo.api.user.people;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class PeopleResource extends EntityModel<People> {

    public PeopleResource(People people, Link... links) {
        super(people, links);
        add(new Link("/profile/"+ people.getUserId()).withSelfRel());
    }
}
