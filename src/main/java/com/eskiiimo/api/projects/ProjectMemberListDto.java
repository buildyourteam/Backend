package com.eskiiimo.api.projects;

import lombok.Getter;

@Getter
public class ProjectMemberListDto {
    private String userName;
    private ProjectRole role;
    private TechnicalStack stack;
    private int level;
}
