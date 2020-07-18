package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.dto.UpdateDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.resource.ProjectDetailResource;
import com.eskiiimo.web.projects.controller.resource.ProjectMemberResource;
import com.eskiiimo.web.projects.controller.resource.RecruitResource;
import com.eskiiimo.web.projects.controller.resource.RecruitsResource;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.service.ProjectDetailService;
import com.eskiiimo.web.projects.validator.ProjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectDetailController {

    @Autowired
    ProjectDetailService projectDetailService;

    @Autowired
    ProjectValidator projectValidator;

    @GetMapping(value = "/{project_id}")
    public ResponseEntity getProjectDetail(
            @PathVariable Long project_id
    ) {
        ProjectDetailDto projectDetailDto = projectDetailService.getProject(project_id);
        ProjectDetailResource projectDetailResource = new ProjectDetailResource(projectDetailDto, project_id);

        Boolean myProject = Boolean.TRUE;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            myProject = isMyProject(projectDetailDto, authentication.getName());

        if (myProject) {
            projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id).withRel("updateProject"));
            projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id).withRel("deleteProject"));
            projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id + "/apply").withRel("applicants"));
        } else
            projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id + "/apply").withRel("apply"));
        projectDetailResource.add(linkTo(DocsController.class).slash("#resourcesProjectGet").withRel("profile"));
        return ResponseEntity.ok(projectDetailResource);
    }

    // 내가 보낸 영입제안 리스트
    @GetMapping(value = "/{project_id}/recruits")
    public ResponseEntity getRecruits(
            @PathVariable Long project_id
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<RecruitDto> recruits = this.projectDetailService.getRecruits(visitorId, project_id);
        List<RecruitResource> recruitResources = new ArrayList<RecruitResource>();
        for (RecruitDto recruitDto : recruits)
            recruitResources.add(new RecruitResource(recruitDto, visitorId));

        RecruitsResource recruitsResource = new RecruitsResource(recruitResources, project_id);
        recruitsResource.add(linkTo(DocsController.class).slash("#getSendRecruits").withRel("profile"));
        return ResponseEntity.ok(recruitsResource);
    }

    @PostMapping
    public ResponseEntity createProject(
            @RequestBody @Valid ProjectDetailDto projectDetailDto
    ) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.projectValidator.validateDate(projectDetailDto);
        Project newProject = this.projectDetailService.storeProject(projectDetailDto, userId);

        RepresentationModel created = new RepresentationModel();
        created.add(linkTo(ProjectDetailController.class).withSelfRel());
        created.add(new Link("/projects/" + newProject.getProjectId()).withRel("createdProject"));
        created.add(linkTo(DocsController.class).slash("#resourcesProjectCreate").withRel("profile"));
        return ResponseEntity.created(linkTo(ProjectDetailController.class).slash(newProject.getProjectId()).toUri()).body(created);
    }

    @PutMapping("/{project_id}")
    public ResponseEntity updateProject(
            @PathVariable Long project_id,
            @RequestBody UpdateDto updateDto
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        ProjectDetailResource projectDetailResource = new ProjectDetailResource(
                projectDetailService.updateProject(project_id, updateDto, visitorId),
                project_id);
        projectDetailResource.add(linkTo(DocsController.class).slash("#resourcesProjectUpdate").withRel("profile"));
        return ResponseEntity.ok(projectDetailResource);
    }

    @DeleteMapping("/{project_id}")
    public ResponseEntity deleteProject(
            @PathVariable Long project_id
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.projectDetailService.deleteProject(project_id, visitorId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    public boolean isMyProject(ProjectDetailDto projectDetailDto, String userId) {
        for (ProjectMemberResource projectMember : projectDetailDto.getMemberList())
            if (projectMember.getContent().getRole().equals(ProjectRole.LEADER))
                if (projectMember.getLinks().getLink("self").get().toString().equals("</profile/" + userId + ">;rel=\"self\""))
                    return Boolean.TRUE;
        return Boolean.FALSE;
    }
}
