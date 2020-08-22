package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.RecruitState;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecruitDto {
    private String userName;
    private String introduction;
    private ProjectRole role;
    private RecruitState state;
    private Long projectId;
    private String projectName;

    public RecruitDto(Recruit recruit) {
        this.userName = recruit.getUser().getUserName();
        this.introduction = recruit.getIntroduction();
        this.role = recruit.getRole();
        this.state = recruit.getState();
        this.projectId = recruit.getProject().getProjectId();
        this.projectName = recruit.getProject().getProjectName();
    }
}
