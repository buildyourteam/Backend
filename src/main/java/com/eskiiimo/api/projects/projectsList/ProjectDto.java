package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectStatus;
import lombok.Getter;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import java.time.LocalDateTime;

@Getter
public class ProjectDto {
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String description;
    private ProjectStatus status;
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
                .status(projectDto.status)
                .current(projectDto.current)
                .needMembers(projectDto.needMembers)
                .build();
    }
}
