package com.eskiiimo.web.projects.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberSet {
    @Column(name = "currentDeveloper")
    private int developer;
    @Column(name = "currentDesigner")
    private int designer;
    @Column(name = "currentPlanner")
    private int planner;
    @Column(name = "currentEtc")
    private int etc;

    public void addMember(ProjectRole role){
        if (role == ProjectRole.DEVELOPER)
            this.developer++;
        else if (role == ProjectRole.DESIGNER)
            this.designer++;
        else if (role == ProjectRole.PLANNER)
            this.planner++;
        else if (role == ProjectRole.ETC)
            this.etc++;
    }
}
