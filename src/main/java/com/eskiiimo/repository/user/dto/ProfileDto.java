package com.eskiiimo.repository.user.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private String userName;
    private ProjectRole role;
    private List<TechnicalStack> stacks;
    private String contact;
    private String area;
    private Long grade;
    private String introduction;
}