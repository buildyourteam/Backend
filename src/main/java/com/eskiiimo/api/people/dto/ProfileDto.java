package com.eskiiimo.api.people.dto;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import lombok.Getter;

@Getter
public class ProfileDto {
    private String userName;
    private String contact;
    private String area;
    private int level;
    private String description;
    private ProjectRole role;
    private TechnicalStack stack;

}
