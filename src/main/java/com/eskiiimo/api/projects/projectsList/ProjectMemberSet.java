package com.eskiiimo.api.projects.projectsList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberSet{
    @Column(name = "currentDeveloper")
    private int developer;
    @Column(name = "currentDesigner")
    private int designer;
    @Column(name = "currentPlanner")
    private int planner;
    @Column(name = "currentEtc")
    private int etc;

}
