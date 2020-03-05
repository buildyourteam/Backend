package com.eskiiimo.api.projects.projectapply.entity;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.projectapply.ProjectApplyStatus;
import com.eskiiimo.api.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="applyId")
@Entity
public class ProjectApply {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long applyId;

    private ProjectApplyStatus status;
    private String selfDescription;
    private Boolean canUploadFile;
    private ProjectRole role;

    @Builder.Default
    @OneToMany
    @JoinColumn(name="applyId")
    private List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
    @ManyToOne
    @JoinColumn(name="accountId")
    private User user;
}
