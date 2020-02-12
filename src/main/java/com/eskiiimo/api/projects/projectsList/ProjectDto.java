package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectField;
import com.eskiiimo.api.projects.ProjectStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ProjectDto {
    @NotEmpty
    private String projectName;
    @NotEmpty
    private String teamName;
    @NotNull
    private LocalDateTime endDate;
    @NotEmpty
    private String description;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectField projectField;
    @Embedded
    private ProjectMemberSet current;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="developer", column = @Column(name="needDeveloper")),
            @AttributeOverride(name="designer", column = @Column(name="needDesigner")),
            @AttributeOverride(name="planner", column = @Column(name="needPlanner")),
            @AttributeOverride(name="etc", column = @Column(name="needEtc"))
    })
    private ProjectMemberSet needMembers;

    public Project toEntity(ProjectDto projectDto) {
        return Project.builder()
                .projectName(projectDto.projectName)
                .teamName(projectDto.teamName)
                .endDate(projectDto.endDate)
                .description(projectDto.description)
                .current(projectDto.current)
                .needMembers(projectDto.needMembers)
                .build();
    }
}
