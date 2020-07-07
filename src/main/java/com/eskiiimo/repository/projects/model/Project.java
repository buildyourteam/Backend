package com.eskiiimo.repository.projects.model;

import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectPersonSet;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@Table(name = "T_PROJECT")
public class Project{

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectId;

    @NotNull
    @Size(min = 0, max = 150)
    private String projectName;

    @NotNull
    @Size(min = 0, max = 20)
    private String teamName;

    @NotNull
    private LocalDateTime endDate;

    @Size(min = 0, max = 10000)
    @NotBlank
    private String projectDescription;

    private long dday;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RecruitStatus recruitStatus = RecruitStatus.RECRUTING;

    @Enumerated(EnumType.STRING)
    private ProjectField projectField;

    @Embedded
    @NotNull
    private ProjectPersonSet currentPerson;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="developer", column = @Column(name="needDeveloper")),
            @AttributeOverride(name="designer", column = @Column(name="needDesigner")),
            @AttributeOverride(name="planner", column = @Column(name="needPlanner")),
            @AttributeOverride(name="etc", column = @Column(name="needEtc"))
    })
    @NotNull
    private ProjectPersonSet needPerson;

    private String leaderId;

    @JsonIgnore
    private Boolean applyCanFile;

    @OneToMany(cascade = {CascadeType.ALL})
    @JsonIgnore
    @JoinColumn(name = "projectId")
    @Builder.Default
    private List<ProjectApply> applies = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL})
    @JsonIgnore
    @JoinColumn(name = "projectId")
    @Builder.Default
    private List<ProjectApplyQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Builder.Default
    private List<ProjectPerson> projectPersons = new ArrayList<>();

    public void addPerson(ProjectPerson person){
        this.projectPersons.add(person);
        if(person.getProject() != this)
            person.setProject(this);
    }
}