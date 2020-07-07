package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectPersonDto {
    private String personName;
    private ProjectRole projectRole;
    private TechnicalStack stack;
    private int personLevel;


    @Builder
    public ProjectPersonDto(String personName, ProjectRole projectRole, TechnicalStack stack, int personLevel){
        this.personName = personName;
        this.projectRole = projectRole;
        this.stack = stack;
        this.personLevel = personLevel;

    }
}
