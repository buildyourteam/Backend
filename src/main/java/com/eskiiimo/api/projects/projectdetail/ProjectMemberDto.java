package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectMemberDto {
    private String userName;
    private ProjectRole role;
    private TechnicalStack stack;
    private int level;


    @Builder
    public ProjectMemberDto(String userName, ProjectRole role, TechnicalStack stack, int level){
        this.userName = userName;
        this.role = role;
        this.stack = stack;
        this.level = level;

    }
}
