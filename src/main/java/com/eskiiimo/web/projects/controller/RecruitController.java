package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.web.index.controller.DocsController;
import com.eskiiimo.web.projects.controller.resource.RecruitResponse;
import com.eskiiimo.web.projects.request.RecruitProjectRequest;
import com.eskiiimo.web.projects.response.GetRecruitsToMeResponse;
import com.eskiiimo.web.projects.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping(value = "/profile/{userId}/recruit", produces = MediaTypes.HAL_JSON_VALUE)
public class RecruitController {

    private final RecruitService recruitService;

    // 프로젝트에 영입하기
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Link recruitProject(
            @PathVariable String userId,
            @RequestBody RecruitProjectRequest recruit,
            HttpServletResponse response
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 프로젝트 영입제안
        this.recruitService.recruitProject(userId, recruit, visitorId);

        response.setHeader("Location",linkTo(RecruitController.class, userId).toUri().toString());
        return linkTo(DocsController.class).slash("#projectRecruit").withRel("self");
    }

    // 나한테 온 영입제안 리스트
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public GetRecruitsToMeResponse getRecruitsToMe(
            @PathVariable String userId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<RecruitDto> recruitList = this.recruitService.getRecruitList(userId, visitorId);
        List<RecruitResponse> recruitResponses = new ArrayList<RecruitResponse>();
        for (RecruitDto recruitDto : recruitList)
            recruitResponses.add(new RecruitResponse(recruitDto, userId));

        return new GetRecruitsToMeResponse(recruitResponses, userId);
    }

    // 나한테 온 영입제안 확인하기(열람시 읽음상태로 전환)
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public RecruitResponse getRecruitProject(
            @PathVariable String userId,
            @PathVariable Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        RecruitResponse recruitResponse = new RecruitResponse(
                this.recruitService.getRecruit(userId, projectId, visitorId),
                userId);
        recruitResponse.add(linkTo(RecruitController.class, userId).slash(projectId).withRel("acceptRecruit"));
        recruitResponse.add(linkTo(RecruitController.class, userId).slash(projectId).withRel("rejectRecruit"));
        recruitResponse.add(linkTo(DocsController.class).slash("#getRecruit").withRel("profile"));

        return recruitResponse;
    }

    // 영입제안 승락하기
    @PutMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public Link acceptRecruitProject(
            @PathVariable String userId,
            @PathVariable Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.recruitService.acceptRecruit(userId, projectId, visitorId);

        return linkTo(DocsController.class).slash("#acceptRecruit").withRel("self");
    }

    // 영입제안 거절하기
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public Link rejectRecruitProject(
            @PathVariable String userId,
            @PathVariable Long projectId
    ) {
        String visitorId = SecurityContextHolder.getContext().getAuthentication().getName();

        this.recruitService.rejectRecruit(userId, projectId, visitorId);

        return linkTo(DocsController.class).slash("#rejectRecruit").withRel("self");
    }
}
