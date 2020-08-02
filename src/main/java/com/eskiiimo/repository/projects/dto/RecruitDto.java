package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.RecruitState;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitDto {
    private String userName;
    private String introduction;
    private ProjectRole role;
    private RecruitState state;
    private Long projectId;
    private String projectName;
}
