package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProfileDto {
    private String userName;
    private String role;
    private String stack;
    private String contact;
    private String area;
    private Long level;
    private String description;

}