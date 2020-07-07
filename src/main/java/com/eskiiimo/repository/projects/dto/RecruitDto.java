package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.web.projects.enumtype.SuggestStatus;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDto {
    private String personName;
    private String selfDescription;
    private ProjectRole projectRole;
    private SuggestStatus suggestStatus;
    private Long projectId;
    private String projectName;

    public Recruit toEntity(Person person, Project project) {
        Recruit recruit = Recruit.builder()
                .projectRole(projectRole)
                .selfDescription(selfDescription)
                .person(person)
                .project(project)
                .suggestStatus(SuggestStatus.UNREAD)
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .build();
        return recruit;
    }
}
