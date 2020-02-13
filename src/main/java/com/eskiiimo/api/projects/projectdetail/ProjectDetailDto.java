package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.projects.ProjectStatus;
import com.eskiiimo.api.projects.projectsList.ProjectMemberSet;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectDetailDto {
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String description;
    private ProjectStatus status;


    private ProjectMemberSet current = new ProjectMemberSet();
    private ProjectMemberSet needMembers= new ProjectMemberSet();;
    private List<ProjectMemberResource> memberList = new ArrayList<ProjectMemberResource>();


    @Builder
    public ProjectDetailDto(String projectName,String teamName, LocalDateTime endDate,String description,ProjectStatus status, ProjectMemberSet currentMembers, ProjectMemberSet needMembers, List<ProjectMemberResource> memberList){
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.description = description;
        this.status = status;
        this.current = currentMembers;
        this.needMembers = needMembers;
        this.memberList = memberList;
    }
}
