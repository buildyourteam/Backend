package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectMemberDto {
    private String userName;
    private ProjectRole role;
    private TechnicalStack stack;
    private Long grade;


    @Builder
    public ProjectMemberDto(String userName, ProjectRole role, TechnicalStack stack, Long grade) {
        this.userName = userName;
        this.role = role;
        this.stack = stack;
        this.grade = grade;

    }
}
