package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.service.ProjectListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectListController {

    @Autowired
    ProjectListService projectListService;
    /*
    직군별 검색기능 value="occupation"
    분야별 검색기능 value="field"
     */

    @GetMapping
    public PagedModel<EntityModel<ProjectListDto>> getProjectsList(
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler,
            @RequestParam(value = "occupation", required = false) String occupation,
            @RequestParam(value = "field", required = false) ProjectField field
    ) {
        return assembler.toModel(this.projectListService.getAllByField(occupation, field, pageable));
    }

    @GetMapping("/deadline")
    public PagedModel<EntityModel<ProjectListDto>> getProjectsDeadline(
            Pageable pageable,
            PagedResourcesAssembler<ProjectListDto> assembler
    ) {
        return assembler.toModel(projectListService.findAllByDdayLessThanOrderByDdayAsc(pageable));
    }


}
