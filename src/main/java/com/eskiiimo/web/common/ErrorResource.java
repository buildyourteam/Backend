package com.eskiiimo.web.common;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

public class ErrorResource extends EntityModel<Errors> {

    public ErrorResource(Errors content, Link... links) {
        super(content, links);
    }

}