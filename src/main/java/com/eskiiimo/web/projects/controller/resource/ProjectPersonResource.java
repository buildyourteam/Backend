package com.eskiiimo.web.projects.controller.resource;

import com.eskiiimo.repository.projects.dto.ProjectPersonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
@Getter
@Setter
@NoArgsConstructor
public class ProjectPersonResource extends EntityModel<ProjectPersonDto> {

    public ProjectPersonResource(ProjectPersonDto projectPerson, String person_id, Link... links) {
        super(projectPerson, links);
        add(new Link("/profile/"+person_id).withSelfRel());
    }
}
