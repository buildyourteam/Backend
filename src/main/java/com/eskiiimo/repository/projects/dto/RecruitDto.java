package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
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
