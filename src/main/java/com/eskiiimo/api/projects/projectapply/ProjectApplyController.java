package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.index.DocsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects/{projectId}/apply", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectApplyController {
    @Autowired
    ProjectApplyService projectApplyService;


    @PostMapping
    ResponseEntity applyProject(@PathVariable Long projectId, @RequestBody ProjectApplyDto apply) throws Exception{
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();
        //지원서 저장
        if(!this.projectApplyService.applyProject(projectId,apply,visitorId))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        return ResponseEntity.created(linkTo(ProjectApplyController.class,projectId).slash(visitorId).toUri()).body(linkTo(DocsController.class).slash("#projectApply").withRel("self"));
    }
    @PutMapping
    ResponseEntity updateApply(@PathVariable Long projectId, @RequestBody ProjectApplyDto apply){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();
        //지원서 저장
        if(!this.projectApplyService.updateApply(projectId,apply,visitorId))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        return ResponseEntity.status(HttpStatus.OK).body(linkTo(DocsController.class).slash("#updateApply").withRel("self"));
    }
    @GetMapping
    ResponseEntity getApplicants(@PathVariable Long projectId, PagedResourcesAssembler<ProjectApplicantDto> assembler){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        // 지원자 리스트 쿼리
        List<ProjectApplicantDto> applicants  = this.projectApplyService.getApplicants(projectId,visitorId);
        if(applicants==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        if(applicants.isEmpty())
            return ResponseEntity.notFound().build();
        //Add Link
        List<ProjectApplicantResource> projectApplicantResources = new ArrayList<ProjectApplicantResource>();
        for(ProjectApplicantDto projectApplicantDto: applicants){
            ProjectApplicantResource projectApplicantResource = new ProjectApplicantResource(projectApplicantDto,projectId);
            projectApplicantResources.add(projectApplicantResource);
        }

        ProjectApplicantsResource projectApplicantsResource =new ProjectApplicantsResource(projectApplicantResources,projectId);
        projectApplicantsResource.add(linkTo(DocsController.class).slash("#getApplicants").withRel("profile"));
        return ResponseEntity.ok(projectApplicantsResource);
    }
    @GetMapping("/{userId}")
    ResponseEntity getApply(@PathVariable Long projectId, @PathVariable String userId){
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
        ProjectApplyResource projectApplyResource = new ProjectApplyResource(projectApplyDto,projectId,userId);
        projectApplyResource.add(linkTo(ProjectApplyController.class,projectId).slash(userId).withRel("acceptApply"));
        projectApplyResource.add(linkTo(ProjectApplyController.class,projectId).slash(userId).withRel("rejectApply"));
        projectApplyResource.add(linkTo(DocsController.class).slash("#getApply").withRel("profile"));

        return ResponseEntity.ok(projectApplyResource);
    }
    @PutMapping("/{userId}")
    ResponseEntity acceptMember(@PathVariable Long projectId, @PathVariable String userId){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        //지원서 쿼리
        if(this.projectApplyService.acceptApply(projectId,userId,visitorId))
            return ResponseEntity.status(HttpStatus.OK).body(linkTo(DocsController.class).slash("#acceptApply").withRel("self"));
        else
            return ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{userId}")
    ResponseEntity rejectMember(@PathVariable Long projectId, @PathVariable String userId){
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        //지원서 쿼리
       if(this.projectApplyService.rejectApply(projectId,userId,visitorId))
            return ResponseEntity.status(HttpStatus.OK).body(linkTo(DocsController.class).slash("#rejecttApply").withRel("self"));
       else
            return ResponseEntity.badRequest().build();
    }
}
