package com.eskiiimo.web.projects.request;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitProjectRequest {
    private String introduction;
    private ProjectRole role;
    private Long projectId;
}
