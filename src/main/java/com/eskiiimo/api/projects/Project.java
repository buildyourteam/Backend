package com.eskiiimo.api.projects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="projectId")
@Entity
public class Project{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectId;
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String description;
    private long dday;
    @Enumerated(EnumType.STRING)
    private Status status = Status.RECRUTING;
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
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "projectId")
    private List<ProjectQuestion> questions = new ArrayList<ProjectQuestion>();


    @Builder.Default
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private List<ProjectMember> projectMembers = new ArrayList<ProjectMember>();


    @OneToOne(cascade = {CascadeType.ALL})
    @JsonIgnore
    private ProjectStatus projectStatus;

    public void addMember(ProjectMember member){
        this.projectMembers.add(member);
        if(member.getProject() != this)
            member.setProject(this);
    }
    public void update() {
        long remainDay = ChronoUnit.DAYS.between(LocalDateTime.now(), this.endDate);
        this.dday = remainDay;
        if (this.dday <= -1) {
            this.status = Status.RECRUTING;
        }

        if (this.getCurrentMember() == null) {
            this.currentMember = new ProjectMemberSet(0, 0, 0, 0);
        }

        if (this.status == null) {
            this.status = Status.RECRUTING;
        }

    }

}