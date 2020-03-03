package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.projectsList.ProjectListResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects/{project_id}/apply", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectApplyController {
    @Autowired
    ProjectApplyService projectApplyService;


    @PostMapping
    ResponseEntity applyProject(@PathVariable String projectId, @RequestBody ProjectApplyDto apply){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();
        //지원서 저장
        if(!this.projectApplyService.applyProject(projectId,apply,visitorId))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @GetMapping
    ResponseEntity getApplicants(@PathVariable String projectId, PagedResourcesAssembler<ProjectApplicantDto> assembler){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        // 지원자 리스트 쿼리
        Page<ProjectApplicantDto> applicantsPage  = this.projectApplyService.getApplicants(projectId,visitorId);
        if(applicantsPage==null)
            return ResponseEntity.notFound().build();
        //Add Link
        PagedModel<ProjectApplicantsResource> projectApplicantsResources = assembler.toModel(applicantsPage, e -> new ProjectApplicantsResource(e));
        projectApplicantsResources.add(linkTo(DocsController.class).slash("#resourcesApplicants").withRel("profile"));
        return ResponseEntity.ok(projectApplicantsResources);
    }
    @GetMapping("/{userId}")
    ResponseEntity getApply(@PathVariable String projectId, @PathVariable String userId){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        //지원서 쿼리
        ProjectApplyDto projectApplyDto = this.projectApplyService.getApply(projectId,userId,visitorId);
        if(projectApplyDto == null)
            return ResponseEntity.notFound().build();

        //Add Link
        ProjectApplyResource projectApplyResource = new ProjectApplyResource(projectApplyDto);
        projectApplyResource.add(linkTo(ProjectApplyController.class).slash(userId).withRel("acceptApply"));
        projectApplyResource.add(linkTo(ProjectApplyController.class).slash(userId).withRel("rejectApply"));
        projectApplyResource.add(linkTo(DocsController.class).slash("#resourcesApplicants").withRel("profile"));

        return ResponseEntity.ok(projectApplyResource);
    }
    @PutMapping("/{userId}")
    ResponseEntity acceptMember(@PathVariable String projectId, @PathVariable String userId){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        //지원서 쿼리
        ProjectApplyDto projectApplyDto = this.projectApplyService.acceptApply(projectId,userId,visitorId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping("/{userId}")
    ResponseEntity rejectMember(@PathVariable String projectId, @PathVariable String userId){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        //지원서 쿼리
        ProjectApplyDto projectApplyDto = this.projectApplyService.rejectApply(projectId,userId,visitorId);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
}
