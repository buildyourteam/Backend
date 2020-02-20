package com.eskiiimo.api.user.profile;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProfileResource extends EntityModel<ProfileDto> {

    public ProfileResource(ProfileDto profile, String user_id, Link... links) {
        super(profile, links);
        add(linkTo(ProfileController.class).slash(user_id).withSelfRel());
    }
}
