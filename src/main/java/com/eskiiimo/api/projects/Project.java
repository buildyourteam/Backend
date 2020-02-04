package com.eskiiimo.api.projects;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class Project{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectId;
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

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private List<ProjectQuestion> questions = new ArrayList<ProjectQuestion>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<ProjectMember> projectMembers = new ArrayList<ProjectMember>();

    public void addMember(ProjectMember member){
        this.projectMembers.add(member);
        if(member.getProject() != this)
            member.setProject(this);
    }

}

