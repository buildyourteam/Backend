package com.eskiiimo.repository.projects.model;

import com.eskiiimo.repository.projects.dto.ModifyProjectDto;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.State;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "projectId")
@Entity
@Table(name = "T_PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private String introduction;
    @Enumerated(EnumType.STRING)
    private State state;
    @Enumerated(EnumType.STRING)
    private ProjectField projectField;
    @Embedded
    @NotNull
    private ProjectMemberSet currentMember;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "developer", column = @Column(name = "needDeveloper")),
            @AttributeOverride(name = "designer", column = @Column(name = "needDesigner")),
            @AttributeOverride(name = "planner", column = @Column(name = "needPlanner")),
            @AttributeOverride(name = "etc", column = @Column(name = "needEtc"))
    })
    @NotNull
    private ProjectMemberSet needMember;

    private String leaderId;

    private Boolean applyCanFile;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "projectId")
    private List<ProjectApply> applies;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "projectId")
    private List<ProjectApplyQuestion> questions;

    @OneToMany(mappedBy = "project")
    private List<ProjectMember> projectMembers;

    public void addMember(ProjectMember member) {
        if (this.projectMembers == null)
            this.projectMembers = new ArrayList<ProjectMember>();
        this.projectMembers.add(member);
        this.currentMember.addMember(member.getRole());
    }

    public void addApply(ProjectApply apply) {
        if (this.applies == null)
            this.applies = new ArrayList<ProjectApply>();
        this.applies.add(apply);
    }

    public void updateApplies(ProjectApply apply) {
        this.applies.set(this.applies.indexOf(apply), apply);
    }

    public void updateProject(ModifyProjectDto modifyProjectDto) {
        this.projectName = modifyProjectDto.getProjectName();
        this.teamName = modifyProjectDto.getTeamName();
        this.endDate = modifyProjectDto.getEndDate();
        this.introduction = modifyProjectDto.getIntroduction();
        this.state = modifyProjectDto.getState();
        this.projectField = modifyProjectDto.getProjectField();
        this.needMember = modifyProjectDto.getNeedMember();
        this.applyCanFile = modifyProjectDto.getApplyCanFile();
        this.questions = modifyProjectDto.getQuestions();
    }

    public Project(ModifyProjectDto modifyProjectDto, String leaderId) {
        this.projectName = modifyProjectDto.getProjectName();
        this.teamName = modifyProjectDto.getTeamName();
        this.endDate = modifyProjectDto.getEndDate();
        this.introduction = modifyProjectDto.getIntroduction();
        this.state = modifyProjectDto.getState();
        this.projectField = modifyProjectDto.getProjectField();
        this.currentMember = new ProjectMemberSet(0, 0, 0, 0);
        this.needMember = modifyProjectDto.getNeedMember();
        this.leaderId = leaderId;
        this.applyCanFile = modifyProjectDto.getApplyCanFile();
        this.questions = modifyProjectDto.getQuestions();
    }
}