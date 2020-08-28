package com.eskiiimo.web.user.response;

import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.user.controller.ProfileController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class UpdateProfileResponse extends EntityModel<ProfileDto> {

    public UpdateProfileResponse(ProfileDto profile, String userId, Link... links) {
        super(profile, links);
        add(linkTo(ProfileController.class).slash(userId).withSelfRel());
        add(linkTo(DocsController.class).slash("#resourcesProfileGet").withRel("profile"));
    }
}
