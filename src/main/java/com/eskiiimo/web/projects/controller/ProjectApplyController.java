package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.ProjectApplicantDto;
import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.resource.ProjectApplicantResource;
import com.eskiiimo.web.projects.request.ProjectApplyRequest;
import com.eskiiimo.web.projects.response.GetApplicantsResponse;
import com.eskiiimo.web.projects.response.ProjectApplyResponse;
import com.eskiiimo.web.projects.service.ProjectApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/projects/{projectId}/apply", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectApplyController {

    private final ProjectApplyService projectApplyService;

    @PostMapping
    public ResponseEntity<Link> applyProject(
            @PathVariable Long projectId,
            @RequestBody ProjectApplyRequest apply
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        //지원서 저장
        this.projectApplyService.applyProject(projectId, apply, visitorId);
        return ResponseEntity
                .created(linkTo(ProjectApplyController.class, projectId).slash(visitorId).toUri())
                .body(linkTo(DocsController.class).slash("#projectApply").withRel("self"));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Link updateApply(
            @PathVariable Long projectId,
            @RequestBody ProjectApplyRequest apply
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        //지원서 수정
        this.projectApplyService.updateApply(projectId, apply, visitorId);

        return linkTo(DocsController.class).slash("#updateApply").withRel("self");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GetApplicantsResponse getApplicants(
            @PathVariable Long projectId,
            PagedResourcesAssembler<ProjectApplicantDto> assembler
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 지원자 리스트 쿼리
        List<ProjectApplicantDto> applicants = this.projectApplyService.getApplicants(projectId, visitorId);

        //Add Link
        List<ProjectApplicantResource> projectApplicantResources = new ArrayList<ProjectApplicantResource>();
        for (ProjectApplicantDto projectApplicantDto : applicants)
            projectApplicantResources.add(new ProjectApplicantResource(projectApplicantDto, projectId));

        return new GetApplicantsResponse(projectApplicantResources, projectId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectApplyResponse getApply(
            @PathVariable Long projectId,
            @PathVariable String userId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        //지원서 쿼리
        ProjectApplyDto projectApplyDto = this.projectApplyService.getApply(projectId, userId, visitorId);

        //Add Link
        ProjectApplyResponse projectApplyResponse = new ProjectApplyResponse(projectApplyDto, projectId, userId);
        projectApplyResponse.add(linkTo(ProjectApplyController.class, projectId).slash(userId).withRel("acceptApply"));
        projectApplyResponse.add(linkTo(ProjectApplyController.class, projectId).slash(userId).withRel("rejectApply"));

        return projectApplyResponse;
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Link acceptMember(
            @PathVariable Long projectId,
            @PathVariable String userId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        //지원서 쿼리
        this.projectApplyService.acceptApply(projectId, userId, visitorId);

        return linkTo(DocsController.class).slash("#acceptApply").withRel("self");
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Link rejectMember(
            @PathVariable Long projectId,
            @PathVariable String userId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        //지원서 쿼리
        this.projectApplyService.rejectApply(projectId, userId, visitorId);

        return linkTo(DocsController.class).slash("#rejecttApply").withRel("self");
    }
}
