package com.eskiiimo.api.projects;

import com.eskiiimo.api.people.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="projectMemberId")
@Entity

public class ProjectMember {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long projectMemberId;
    private ProjectRole role;
    private TechnicalStack stack;
    @Lob
    private String selfDescription;

    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectMemberId")
    private List<ProjectAnswer> answers = new ArrayList<ProjectAnswer>();

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    public void setMember(Member member){
        this.member = member;
        if(!member.getProjects().contains(this))
        member.getProjects().add(this);
    }

    public void setProject(Project project){
        this.project = project;
        if(!project.getProjectMembers().contains(this))
            project.getProjectMembers().add(this);
    }
}
