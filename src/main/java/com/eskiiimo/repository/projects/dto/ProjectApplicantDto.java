package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.ProjectApplyStatus;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectApplicantDto {
    private String personId;
    private String personName;
    private ProjectApplyStatus projectApplyStatus;
    private ProjectRole projectRole;
    @Builder
    public ProjectApplicantDto(String personId,String personName,ProjectApplyStatus projectApplyStatus,ProjectRole projectRole){
        this.personId = personId;
        this.personName = personName;
        this.projectApplyStatus = projectApplyStatus;
        this.projectRole = projectRole;
    }
}
