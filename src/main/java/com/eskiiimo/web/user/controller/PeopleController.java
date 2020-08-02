package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.user.dto.PeopleDto;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.user.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/people")
public class PeopleController {

    private final PeopleService peopleService;

    @GetMapping
    public PagedModel<EntityModel<PeopleDto>> getJobSeekers(
            Pageable pageable,
            PagedResourcesAssembler<PeopleDto> assembler,
            @RequestParam(value = "grade", required = false) Long grade,
            @RequestParam(value = "role", required = false) ProjectRole role,
            @RequestParam(value = "area", required = false) String area
    ) {
        return assembler.toModel(peopleService.getPeople(grade, role, area, pageable));
    }
}
