package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectMemberDto {
    private String userName;
    private Long grade;
    private ProjectRole role;
    private TechnicalStack stack;

    public ProjectMemberDto(ProjectMember projectMember) {
        this.userName = projectMember.getUser().getUserName();
        this.grade = projectMember.getUser().getGrade();
        this.role = projectMember.getRole();
        this.stack = projectMember.getStack();
    }
}
