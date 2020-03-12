package com.eskiiimo.api.user.projectrecruit;

import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.detail.ProjectDetailController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
@RequestMapping(value = "/profile/{userId}/recruit", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectRecruitController {
    @Autowired
    ProjectRecruitService projectRecruitService;

    // 프로젝트에 영입하기
    @PostMapping("/{projectId}")
    public ResponseEntity recruitProject(@PathVariable String userId, @PathVariable Long projectId,
                                  @RequestBody ProjectRecruitDto recruit) {
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();

        // 프로젝트 영입제안
        try{
            this.projectRecruitService.recruitProject(userId, recruit, projectId, visitorId);
            return ResponseEntity.created(linkTo(ProjectRecruitController.class, userId).slash(projectId).toUri()).body(linkTo(DocsController.class).slash("#projectRecruit").withRel("self"));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 나한테 온 영입제안 리스트
    @GetMapping
    public ResponseEntity getRecruitList(@PathVariable String userId) {
        // 계정 확인
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();
        List<ProjectRecruitDto> recruitList=this.projectRecruitService.getRecruitList(userId, visitorId);
        List<ProjectRecruitResource> projectRecruitResources=new ArrayList<ProjectRecruitResource>();
        for(ProjectRecruitDto projectRecruitDto : recruitList){
            ProjectRecruitResource projectRecruitResource = new ProjectRecruitResource(projectRecruitDto, userId);
            projectRecruitResources.add(projectRecruitResource);
        }
        ProjectRecruitListResource projectRecruitListResource = new ProjectRecruitListResource(projectRecruitResources, userId);
        projectRecruitListResource.add(linkTo(DocsController.class).slash("#getRecruits").withRel("profile"));
        return ResponseEntity.ok(projectRecruitListResource);
    }

    // 나한테 온 영입제안 확인하기(열람시 읽음상태로 전환)
    @GetMapping("/{projectId}")
    public ResponseEntity getRecruitProject(@PathVariable String userId, @PathVariable Long projectId) {
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();
        ProjectRecruitDto projectRecruitDto = this.projectRecruitService.getRecruit(userId, projectId, visitorId);
        ProjectRecruitResource projectRecruitResource = new ProjectRecruitResource(projectRecruitDto, userId);
        projectRecruitResource.add(linkTo(ProjectRecruitController.class, userId).slash(projectId).withRel("acceptRecruit"));
        projectRecruitResource.add(linkTo(ProjectRecruitController.class, userId).slash(projectId).withRel("rejectRecruit"));
        projectRecruitResource.add(linkTo(DocsController.class).slash("#getRecruit").withRel("profile"));
        return ResponseEntity.ok(projectRecruitResource);
    }

    // 영입제안 승락하기
    @PutMapping("/{projectId}")
    public ResponseEntity acceptRecruitProject(@PathVariable String userId, @PathVariable Long projectId) {
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();
        try{
            this.projectRecruitService.acceptRecruit(userId, projectId, visitorId);
            return ResponseEntity.ok().body(linkTo(DocsController.class).slash("#acceptRecruit").withRel("self"));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 영입제안 거절하기
    @DeleteMapping("/{projectId}")
    public ResponseEntity rejectRecruitProject(@PathVariable String userId, @PathVariable Long projectId) {
        // 계정 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String visitorId = authentication.getName();
        try{
            this.projectRecruitService.rejectRecruit(userId, projectId, visitorId);
            return ResponseEntity.ok().body(linkTo(DocsController.class).slash("#rejectRecruit").withRel("self"));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
