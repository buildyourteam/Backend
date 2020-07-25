package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.resource.RecruitListResource;
import com.eskiiimo.web.projects.controller.resource.RecruitResource;
import com.eskiiimo.web.projects.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/profile/{userId}/recruit", produces = MediaTypes.HAL_JSON_VALUE)
public class RecruitController {
    @Autowired
    RecruitService recruitService;

    // 프로젝트에 영입하기
    @PostMapping
    public ResponseEntity recruitProject(
            @PathVariable String userId,
            @RequestBody RecruitDto recruit
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 프로젝트 영입제안
        this.recruitService.recruitProject(userId, recruit, visitorId);
        return ResponseEntity.created(linkTo(RecruitController.class, userId).toUri()).body(linkTo(DocsController.class).slash("#projectRecruit").withRel("self"));
    }

    // 나한테 온 영입제안 리스트
    @GetMapping
    public ResponseEntity getRecruitList(
            @PathVariable String userId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<RecruitDto> recruitList = this.recruitService.getRecruitList(userId, visitorId);
        List<RecruitResource> recruitResources = new ArrayList<RecruitResource>();
        for (RecruitDto recruitDto : recruitList)
            recruitResources.add(new RecruitResource(recruitDto, userId));

        RecruitListResource recruitListResource = new RecruitListResource(recruitResources, userId);
        recruitListResource.add(linkTo(DocsController.class).slash("#getRecruits").withRel("profile"));
        return ResponseEntity.ok(recruitListResource);
    }

    // 나한테 온 영입제안 확인하기(열람시 읽음상태로 전환)
    @GetMapping("/{projectId}")
    public ResponseEntity getRecruitProject(
            @PathVariable String userId,
            @PathVariable Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        RecruitResource recruitResource = new RecruitResource(
                this.recruitService.getRecruit(userId, projectId, visitorId),
                userId);
        recruitResource.add(linkTo(RecruitController.class, userId).slash(projectId).withRel("acceptRecruit"));
        recruitResource.add(linkTo(RecruitController.class, userId).slash(projectId).withRel("rejectRecruit"));
        recruitResource.add(linkTo(DocsController.class).slash("#getRecruit").withRel("profile"));
        return ResponseEntity.ok(recruitResource);
    }

    // 영입제안 승락하기
    @PutMapping("/{projectId}")
    public ResponseEntity acceptRecruitProject(@PathVariable String userId, @PathVariable Long projectId) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.recruitService.acceptRecruit(userId, projectId, visitorId);
        return ResponseEntity.ok().body(linkTo(DocsController.class).slash("#acceptRecruit").withRel("self"));
    }

    // 영입제안 거절하기
    @DeleteMapping("/{projectId}")
    public ResponseEntity rejectRecruitProject(@PathVariable String userId, @PathVariable Long projectId) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.recruitService.rejectRecruit(userId, projectId, visitorId);
        return ResponseEntity.ok().body(linkTo(DocsController.class).slash("#rejectRecruit").withRel("self"));
    }
}
