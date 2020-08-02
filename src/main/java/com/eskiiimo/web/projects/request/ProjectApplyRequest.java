package com.eskiiimo.web.projects.request;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectApplyRequest {
    private List<String> answers;
    private String introduction;
    private ProjectRole role;
}
