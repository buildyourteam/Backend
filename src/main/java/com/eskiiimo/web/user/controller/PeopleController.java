package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.user.model.People;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.user.controller.resource.PeopleResource;
import com.eskiiimo.web.user.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;


@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping(value ="/people")
public class PeopleController {

    @Autowired
    PeopleService peopleService;


    @GetMapping
    public ResponseEntity getJobSeekers(Pageable pageable, PagedResourcesAssembler<People> assembler,
                                        @RequestParam(value = "level", required = false)Long level,
                                        @RequestParam(value = "role",required =  false) ProjectRole role,
                                        @RequestParam(value = "area",required = false)String area
    ) {
        Page<People> page = peopleService.getPeople(level,role,area,pageable);
        PagedModel<PeopleResource> pagedResources = assembler.toModel(page, e -> new PeopleResource(e));
        pagedResources.add(linkTo(DocsController.class).slash("resourcesPeople").withRel("profile"));

        return ResponseEntity.ok(pagedResources);

    }
}
