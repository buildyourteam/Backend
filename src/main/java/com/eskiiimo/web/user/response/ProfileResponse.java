package com.eskiiimo.web.user.response;

import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.user.controller.ProfileController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class ProfileResponse extends EntityModel<ProfileDto> {

    public ProfileResponse(ProfileDto profile, String user_id, Link... links) {
        super(profile, links);
        add(linkTo(ProfileController.class).slash(user_id).withSelfRel());
    }
}
