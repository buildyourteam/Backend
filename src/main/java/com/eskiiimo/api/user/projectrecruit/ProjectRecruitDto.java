package com.eskiiimo.api.user.projectrecruit;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.apply.ProjectApplyStatus;
import com.eskiiimo.api.user.User;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRecruitDto {
    private String userName;
    private String selfDescription;
    private ProjectRole role;
    private ProjectRecruitStatus status;
    private Long projectId;
    private String projectName;

    public ProjectRecruit toEntity(User user, Project project) {
        ProjectRecruit projectRecruit = ProjectRecruit.builder()
                .role(role)
                .selfDescription(selfDescription)
                .user(user)
                .project(project)
                .status(ProjectRecruitStatus.UNREAD)
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .build();
        return projectRecruit;
    }
}
