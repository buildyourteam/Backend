package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.projects.ProjectField;
import com.eskiiimo.api.projects.Status;
import com.eskiiimo.api.projects.ProjectMemberSet;
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
    private Status status;
    private long dday;
    private ProjectField projectField;

    private ProjectMemberSet currentMember = new ProjectMemberSet();
    private ProjectMemberSet needMember= new ProjectMemberSet();
    private List<ProjectMemberResource> memberList = new ArrayList<ProjectMemberResource>();


    @Builder
    public ProjectDetailDto(String projectName, String teamName, LocalDateTime endDate, String description, Status status, ProjectMemberSet currentMember, ProjectMemberSet needMember, List<ProjectMemberResource> memberList, long dday, ProjectField projectField){
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.description = description;
        this.status = status;
        this.currentMember = currentMember;
        this.needMember = needMember;
        this.memberList = memberList;
        this.dday=dday;
        this.projectField=projectField;
    }
}
