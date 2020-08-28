package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.user.dto.PeopleDto;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.user.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 팀을 구하는 사람들 컨트롤러
 *
 * @author always0ne
 * @version 1.0
 */
@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/people")
public class PeopleController {

    private final PeopleService peopleService;

    /**
     * 팀을 구하는사람들 리스트 구하기
     *
     * @param pageable  페이징 정보
     * @param assembler page 데이터
     * @param grade     점수 검색(Nullable)
     * @param role      역할 검색(Nullable)
     * @param area      지역 검색(Nullable)
     * @return People List
     * @see PeopleService#getPeople(Long, ProjectRole, String, Pageable) PeopleService.getPeople
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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
