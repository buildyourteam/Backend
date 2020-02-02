package com.eskiiimo.api.dto;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import lombok.Getter;

@Getter
public class MemberListDto {
    private Long id;
    private String userName;
    private ProjectRole role;
    private TechnicalStack stack;
    private int level;
}
