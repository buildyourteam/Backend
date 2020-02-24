package com.eskiiimo.api.user.people;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
                                        @RequestParam(value = "role",required =  false)String role,
                                        @RequestParam(value = "area",required = false)String area
    ) {
        Page<People> page = peopleService.getPeople(level,role,area,pageable);
        PagedModel<PeopleResource> pagedResources = assembler.toModel(page, e -> new PeopleResource(e));
        pagedResources.add(new Link("/docs/index.html#resourcesPeople").withRel("profile"));

        return ResponseEntity.ok(pagedResources);

    }
}
