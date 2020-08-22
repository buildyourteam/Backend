package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectApplicantDto {
    private String userId;
    private String userName;
    private ProjectApplyState state;
    private ProjectRole role;

    public ProjectApplicantDto(ProjectApply projectApply) {
        this.userId = projectApply.getUser().getUserId();
        this.userName = projectApply.getUser().getUserName();
        this.state = projectApply.getState();
        this.role = projectApply.getRole();
    }
}
