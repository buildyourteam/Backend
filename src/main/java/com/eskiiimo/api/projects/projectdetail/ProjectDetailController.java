package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.common.ErrorResource;
import com.eskiiimo.api.index.DocsController;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectMember;
import com.eskiiimo.api.projects.ProjectRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/projects", produces = MediaTypes.HAL_JSON_VALUE)
public class ProjectDetailController {

    @Autowired
    ProjectDetailService projectDetailService;

    @Autowired
    ModelMapper modelMapper;


    @GetMapping(value = "/{project_id}")
    public ResponseEntity getProjectDetail(@PathVariable Long project_id){
        ProjectDetailDto projectDetailDto = projectDetailService.getProject(project_id);
        if(projectDetailDto == null)
            return ResponseEntity.notFound().build();
        ProjectDetailResource projectDetailResource = new ProjectDetailResource(projectDetailDto,project_id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean myProject = Boolean.TRUE;
        if(authentication!=null) {
            String userId = authentication.getName();
            myProject=isMyProject(projectDetailDto,userId);
        }
        if(myProject){
            projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id).withRel("updateProject"));
            projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id).withRel("deleteProject"));
        }
        else
            projectDetailResource.add(linkTo(ProjectDetailController.class).slash(project_id+"/apply").withRel("apply"));
        projectDetailResource.add(linkTo(DocsController.class).slash("#resourcesProjectGet").withRel("profile"));
        return ResponseEntity.ok(projectDetailResource);
    }

    @PostMapping
    public ResponseEntity createProject(@RequestBody @Valid ProjectDetailDto projectDetailDto, Errors errors) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String userId = authentication.getName();
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        /*
        ProjectDetail validator
         */
        Project newProject = this.projectDetailService.storeProject(projectDetailDto,userId);
        ControllerLinkBuilder selfLinkBuilder = linkTo(ProjectDetailController.class).slash(newProject.getProjectId());
        URI createdUri = selfLinkBuilder.toUri();
        RepresentationModel created = new RepresentationModel();
        created.add(WebMvcLinkBuilder.linkTo(ProjectDetailController.class).withSelfRel());
        created.add(new Link("/projects/"+newProject.getProjectId()).withRel("createdProject"));
        created.add(linkTo(DocsController.class).slash("#resourcesProjectCreate").withRel("profile"));
        return ResponseEntity.created(createdUri).body(created);
    }

    @PutMapping("/{project_id}")
    public ResponseEntity updateProject(@PathVariable Long project_id,
                                        @RequestBody ProjectDetailDto projectDetailDto,
                                        Errors errors) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String userId = authentication.getName();
        ProjectDetailDto project = projectDetailService.updateProject(project_id, projectDetailDto,userId);
        if(project == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        ProjectDetailResource projectDetailResource = new ProjectDetailResource(project, project_id);
        projectDetailResource.add(linkTo(DocsController.class).slash("#resourcesProjectUpdate").withRel("profile"));
        return ResponseEntity.ok(projectDetailResource);
    }

    @DeleteMapping("/{project_id}")
    public ResponseEntity deleteProject(@PathVariable Long project_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String userId = authentication.getName();
        if(this.projectDetailService.deleteProject(project_id,userId))
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }
    public boolean isMyProject(ProjectDetailDto projectDetailDto, String userId){
        for(ProjectMemberResource projectMember :projectDetailDto.getMemberList()){
            if(projectMember.getContent().getRole().equals(ProjectRole.LEADER)){
                System.out.println(projectMember.getLinks().getLink("self").get().toString());
                if(projectMember.getLinks().getLink("self").get().toString().equals("</profile/"+userId+">;rel=\"self\"")) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorResource(errors));
    }
}
