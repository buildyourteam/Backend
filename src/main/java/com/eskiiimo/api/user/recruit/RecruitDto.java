package com.eskiiimo.api.user.recruit;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.user.User;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDto {
    private String userName;
    private String selfDescription;
    private ProjectRole role;
    private RecruitStatus status;
    private Long projectId;
    private String projectName;

    public Recruit toEntity(User user, Project project) {
        Recruit recruit = Recruit.builder()
                .role(role)
                .selfDescription(selfDescription)
                .user(user)
                .project(project)
                .status(RecruitStatus.UNREAD)
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .build();
        return recruit;
    }
}
