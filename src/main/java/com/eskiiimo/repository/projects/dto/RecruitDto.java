package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.RecruitState;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDto {
    private String userName;
    private String introduction;
    private ProjectRole role;
    private RecruitState state;
    private Long projectId;
    private String projectName;

    public Recruit toEntity(User user, Project project) {
        Recruit recruit = Recruit.builder()
                .role(role)
                .introduction(introduction)
                .user(user)
                .project(project)
                .state(RecruitState.UNREAD)
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .build();
        return recruit;
    }
}
