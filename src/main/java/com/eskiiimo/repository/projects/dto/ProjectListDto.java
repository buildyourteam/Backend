package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@Data
@AllArgsConstructor
@Getter
@Setter
public class ProjectListDto {
    @NotEmpty
    private String projectName;
    @NotEmpty
    private String teamName;
    @NotNull
    private LocalDateTime endDate;
    @NotEmpty
    private String introduction;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ProjectField projectField;
    @Embedded
    private ProjectMemberSet currentMember;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="developer", column = @Column(name="needDeveloper")),
            @AttributeOverride(name="designer", column = @Column(name="needDesigner")),
            @AttributeOverride(name="planner", column = @Column(name="needPlanner")),
            @AttributeOverride(name="etc", column = @Column(name="needEtc"))
    })
    private ProjectMemberSet needMember;

    public Project toEntity(ProjectListDto projectListDto) {
        return Project.builder()
                .projectName(projectListDto.projectName)
                .teamName(projectListDto.teamName)
                .endDate(projectListDto.endDate)
                .introduction(projectListDto.introduction)
                .currentMember(projectListDto.currentMember)
                .needMember(projectListDto.needMember)
                .build();
    }
}
