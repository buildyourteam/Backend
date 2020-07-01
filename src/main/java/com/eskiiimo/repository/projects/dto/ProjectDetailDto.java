package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.web.projects.controller.resource.ProjectMemberResource;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.State;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectDetailDto {
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String introduction;
    private State state;
    private long dday;
    private ProjectField projectField;
    private Boolean applyCanFile;
    private List<String> questions = new ArrayList<String>();
    private ProjectMemberSet currentMember = new ProjectMemberSet();
    private ProjectMemberSet needMember= new ProjectMemberSet();
    private List<ProjectMemberResource> memberList = new ArrayList<ProjectMemberResource>();


    @Builder
    public ProjectDetailDto(String projectName, String teamName, LocalDateTime endDate, String introduction, State state, ProjectMemberSet currentMember, ProjectMemberSet needMember, List<ProjectMember> memberList, long dday, ProjectField projectField, Boolean applyCanFile, List<ProjectApplyQuestion> questions){
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.introduction = introduction;
        this.state = state;
        this.currentMember = currentMember;
        this.needMember = needMember;
        this.dday=dday;
        this.projectField=projectField;
        this.applyCanFile = applyCanFile;
        for(ProjectApplyQuestion question : questions)
            this.questions.add(question.getQuestion());
        if(memberList ==null)
            this.memberList = null;
        else {
            List<ProjectMemberResource> projectMemberListResource = new ArrayList<ProjectMemberResource>();
            if(!memberList.isEmpty())
                for (ProjectMember projectMember : memberList) {
                    ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
                            .userName(projectMember.getUser().getUserName())
                            .role(projectMember.getRole())
                            .stack(projectMember.getStack())
                            .build();
                    ProjectMemberResource projectMemberResource = new ProjectMemberResource(projectMemberDto, projectMember.getUser().getUserId());
                    projectMemberListResource.add(projectMemberResource);
                }
            this.memberList = projectMemberListResource;
        }
    }

    public Project toEntity(Project project){
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        for(String question : this.questions)
            questions.add(ProjectApplyQuestion.builder().question(question).build());
                project.setProjectName(this.projectName);
                project.setTeamName(this.teamName);
                project.setEndDate(this.endDate);
                project.setIntroduction(this.introduction);
                if(this.state==null)
                    project.setState(State.RECRUTING);
                else
                    project.setState(this.state);
                project.setDday(ChronoUnit.DAYS.between(LocalDateTime.now(), this.endDate));
                project.setProjectField(this.projectField);
                project.setApplyCanFile(this.applyCanFile);
                project.setQuestions(questions);
                if(this.currentMember==null)
                    project.setCurrentMember(new ProjectMemberSet(0,0,0,0));
                project.setNeedMember(this.needMember);

        return project;
    }
}
