package com.restful.api.Projects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
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
