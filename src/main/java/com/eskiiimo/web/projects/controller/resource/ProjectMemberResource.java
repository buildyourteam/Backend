package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.repository.projects.dto.ProjectMemberDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
@Getter
@Setter
@NoArgsConstructor
public class ProjectMemberResource extends EntityModel<ProjectMemberDto> {

    public ProjectMemberResource(ProjectMemberDto projectMember, String user_id, Link... links) {
        super(projectMember, links);
        add(new Link("/profile/"+user_id).withSelfRel());
    }
}
