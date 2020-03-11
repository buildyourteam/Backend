package com.eskiiimo.api.projects;

import com.eskiiimo.api.projects.apply.entity.ProjectApply;
import com.eskiiimo.api.projects.apply.entity.ProjectApplyQuestion;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Builder.Default
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

    @JsonIgnore
    private Boolean applyCanFile;

    @Builder.Default
    @OneToMany(cascade = {CascadeType.ALL})
    @JsonIgnore
    @JoinColumn(name = "projectId")
    private List<ProjectApply> applies = new ArrayList<ProjectApply>();

    @Builder.Default
    @OneToMany(cascade = {CascadeType.ALL})
    @JsonIgnore
    @JoinColumn(name = "projectId")
    private List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();

    @Builder.Default
    @OneToMany(mappedBy = "project",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<ProjectMember> projectMembers = new ArrayList<ProjectMember>();

    public void addMember(ProjectMember member){
        this.projectMembers.add(member);
        if(member.getProject() != this)
            member.setProject(this);
    }
}