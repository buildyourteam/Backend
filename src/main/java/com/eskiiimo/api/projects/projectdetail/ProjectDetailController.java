package com.eskiiimo.api.projects.projectdetail;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/projects", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectDetailController {


    private final ProjectDetailService projectDetailService;

    public ProjectDetailController(ProjectDetailService projectDetailService) {
        this.projectDetailService = projectDetailService;
    }


    @GetMapping(value = "/{project_id}")
    public ResponseEntity getProjectDetail(@PathVariable Long project_id){
        ProjectDetailDto projectDetailDto = projectDetailService.getProject(project_id);
        ProjectDetailResource projectDetailResource = new ProjectDetailResource(projectDetailDto,project_id);
        projectDetailResource.add(new Link("/api/projects/"+project_id+"/apply").withRel("apply"));
        projectDetailResource.add(new Link("/docs/index.html#resources-project-get").withRel("profile"));
        return ResponseEntity.ok(projectDetailResource);
    }
}
