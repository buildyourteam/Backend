package com.eskiiimo.web.user.response;

import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.RecruitController;
import com.eskiiimo.web.user.controller.ProfileController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class GetProfileResponse extends EntityModel<ProfileDto> {

    public GetProfileResponse(ProfileDto profile, String userId, String visitorId, Link... links) {
        super(profile, links);
        if (userId.equals(visitorId)) {
            add(linkTo(ProfileController.class).slash(userId).withRel("updateProfile"));
            add(linkTo(RecruitController.class, userId).withRel("recruits"));
        }
        add(linkTo(ProfileController.class).slash(userId).withSelfRel());
        add(linkTo(DocsController.class).slash("#resourcesProfileGet").withRel("profile"));
    }
}
