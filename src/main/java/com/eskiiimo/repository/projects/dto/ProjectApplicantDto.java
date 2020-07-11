package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectApplicantDto {
    private String userId;
    private String userName;
    private ProjectApplyState state;
    private ProjectRole role;
    @Builder
    public ProjectApplicantDto(String userId, String userName, ProjectApplyState state, ProjectRole role){
        this.userId = userId;
        this.userName = userName;
        this.state = state;
        this.role = role;
    }
}
