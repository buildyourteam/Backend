package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.resource.ProjectMemberResource;
import com.eskiiimo.web.projects.controller.resource.RecruitResponse;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import com.eskiiimo.web.projects.response.GetRecruitsResponse;
import com.eskiiimo.web.projects.response.ProjectDetailResponse;
import com.eskiiimo.web.projects.service.ProjectDetailService;
import com.eskiiimo.web.projects.validator.ProjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectDetailController {

    private final ProjectDetailService projectDetailService;

    private final ProjectValidator projectValidator;

    @GetMapping(value = "/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetailResponse getProjectDetail(
            @PathVariable Long projectId
    ) {
        ProjectDetailDto projectDetailDto = projectDetailService.getProject(projectId);
        ProjectDetailResponse projectDetailResponse = new ProjectDetailResponse(projectDetailDto, projectId);

        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (isMyProject(projectDetailDto, visitorId)) {
            projectDetailResponse.add(linkTo(ProjectDetailController.class).slash(projectId).withRel("updateProject"));
            projectDetailResponse.add(linkTo(ProjectDetailController.class).slash(projectId).withRel("deleteProject"));
            projectDetailResponse.add(linkTo(ProjectDetailController.class).slash(projectId + "/apply").withRel("getApplicants"));
        } else
            projectDetailResponse.add(linkTo(ProjectDetailController.class).slash(projectId + "/apply").withRel("apply"));
        projectDetailResponse.add(linkTo(DocsController.class).slash("#resourcesProjectGet").withRel("profile"));

        return projectDetailResponse;
    }

    // 내가 보낸 영입제안 리스트
    @GetMapping(value = "/{projectId}/recruits")
    @ResponseStatus(HttpStatus.OK)
    public GetRecruitsResponse getRecruits(
            @PathVariable Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<RecruitDto> recruits = this.projectDetailService.getRecruits(visitorId, projectId);
        List<RecruitResponse> recruitResponses = new ArrayList<RecruitResponse>();
        for (RecruitDto recruitDto : recruits)
            recruitResponses.add(new RecruitResponse(recruitDto, visitorId));

        return new GetRecruitsResponse(recruitResponses, projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Link createProject(
            @RequestBody @Valid ProjectDetailRequest projectDetailRequest,
            HttpServletResponse response
    ) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.projectValidator.validateDate(projectDetailRequest);
        Project newProject = this.projectDetailService.storeProject(projectDetailRequest, userId);

        response.setHeader("Location", linkTo(ProjectDetailController.class).slash(newProject.getProjectId()).toUri().toString());
        return linkTo(DocsController.class).slash("#resourcesProjectCreate").withRel("profile");
    }

    @PutMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectDetailResponse updateProject(
            @PathVariable Long projectId,
            @RequestBody ProjectDetailRequest projectDetailRequest
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();
        ProjectDetailDto project = projectDetailService.updateProject(projectId, projectDetailRequest, visitorId);
        ProjectDetailResponse projectDetailResponse = new ProjectDetailResponse(project, projectId);
        projectDetailResponse.add(linkTo(DocsController.class).slash("#resourcesProjectUpdate").withRel("profile"));

        return projectDetailResponse;
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(
            @PathVariable Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.projectDetailService.deleteProject(projectId, visitorId);
    }

    public boolean isMyProject(ProjectDetailDto projectDetailDto, String userId) {
        for (ProjectMemberResource projectMember : projectDetailDto.getMemberList())
            if (projectMember.getContent().getRole().equals(ProjectRole.LEADER))
                if (projectMember.getLinks().getLink("self").get().toString().equals("</profile/" + userId + ">;rel=\"self\""))
                    return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
