package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping(value="/api/projects", produces= MediaTypes.HAL_JSON_VALUE)
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;


    /*
    직군별 검색기능 value="occupation"
     */

    @GetMapping
    public ResponseEntity getProjectsList(Pageable pageable, PagedResourcesAssembler<Project> assembler,
                                          @RequestParam(value="occupation", required=false) String occupation) {
        Page<Project> page = this.projectRepository.findAll(pageable);
        if(occupation !=null) {
            if (occupation.equals("developer")) {
                page = this.projectRepository.findAllByNeedMembersDeveloperGreaterThan(0, pageable);

            }
            else if (occupation.equals("designer")) {
                page = this.projectRepository.findAllByNeedMembersDesignerGreaterThan(0, pageable);

            }
            else if (occupation.equals("planner")) {
                page = this.projectRepository.findAllByNeedMembersPlannerGreaterThan(0, pageable);

            }
            else if (occupation.equals("etc")) {
                page = this.projectRepository.findAllByNeedMembersEtcGreaterThan(0, pageable);

            }

        }
        PagedModel<ProjectResource> pagedResources = assembler.toModel(page, e -> new ProjectResource(e));
        pagedResources.add(new Link("/api/projects").withRel("project-list"));
        pagedResources.add(new Link("/html5/index.html#resources-projects-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

//    @GetMapping("/deadline")
//    public ResponseEntity getProjectsDeadline(Pageable pageable, PagedResourcesAssembler<Project> assembler) {

//        Page<Project> page = this.projectRepository.findTop2OrderByEndDate(pageable);
//        PagedModel<ProjectResource> pagedResources = assembler.toModel(page, e -> new ProjectResource(e));
//        pagedResources.add(new Link("/api/projects/deadline").withRel("project-list-deadline"));

//        return ResponseEntity.ok(pagedResources);

//    }
}
