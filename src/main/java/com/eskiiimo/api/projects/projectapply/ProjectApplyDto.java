package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApplyAnswer;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApply;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApplyQuestion;
import com.eskiiimo.api.user.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectApplyDto {
    private String userName;
    private ProjectApplyStatus status;
    private List<ProjectApplyQuestion> questions;
    private List<ProjectApplyAnswer> answers;
    private String selfDescription;
    private Boolean canUploadFile;
    private ProjectRole role;

    @Builder
    public ProjectApplyDto(String userName, ProjectApplyStatus status, List<ProjectApplyQuestion> questions, List<ProjectApplyAnswer> answers, String selfDescription, Boolean canUploadFile, ProjectRole role){
        this.userName = userName;
        this.status = status;
        this.questions = questions;
        this.answers =answers;
        this.selfDescription = selfDescription;
        this.canUploadFile = canUploadFile;
        this.role = role;
    }
    public ProjectApply toEntity(User user){
        ProjectApply projectApply = ProjectApply.builder()
                .answers(this.answers)
                .selfDescription(this.selfDescription)
                .canUploadFile(this.canUploadFile)
                .status(ProjectApplyStatus.UNREAD)
                .user(user)
                .role(role)
                .build();
        return projectApply;
    }
}
