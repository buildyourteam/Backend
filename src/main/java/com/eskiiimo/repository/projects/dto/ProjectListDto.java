package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Relation(collectionRelation = "projectList")
public class ProjectListDto {
    private Long projectId;
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String introduction;
    private State state;
    private ProjectField projectField;
    private ProjectMemberSet currentMember;
    private ProjectMemberSet needMember;
    private String leaderId;
}
