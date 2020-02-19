package com.eskiiimo.api.user.people;

import com.eskiiimo.api.projects.projectsList.ProjectResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequiredArgsConstructor
public class PeopleController {

    @Autowired
    PeopleService peopleService;


    @GetMapping("/api/people")
    public ResponseEntity getJobSeekers(Pageable pageable, PagedResourcesAssembler<PeopleDto> assembler,
                                        @RequestParam(value = "level", required = false)Long level,
                                        @RequestParam(value = "role",required =  false)String role,
                                        @RequestParam(value = "area",required = false)String area
    ) {
        Page<PeopleDto> page = peopleService.getPeople(level,role,area,pageable);
        PagedModel<PeopleResource> pagedResources = assembler.toModel(page, e -> new PeopleResource(e));
        pagedResources.add(new Link("/docs/index.html#resources-people").withRel("profile"));

        return ResponseEntity.ok(pagedResources);

    }
}
