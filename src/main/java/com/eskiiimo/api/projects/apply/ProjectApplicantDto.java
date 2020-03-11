package com.eskiiimo.api.projects.apply;

import com.eskiiimo.api.projects.ProjectRole;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectApplicantDto {
    private String userId;
    private String userName;
    private ProjectApplyStatus status;
    private ProjectRole role;
    @Builder
    public ProjectApplicantDto(String userId,String userName,ProjectApplyStatus status,ProjectRole role){
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.role = role;
    }
}
