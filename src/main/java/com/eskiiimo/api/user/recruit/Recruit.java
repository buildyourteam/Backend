package com.eskiiimo.api.user.recruit;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.user.User;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="recruitId")
@Entity
public class Recruit {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long recruitId;
    @Enumerated(EnumType.STRING)
    private RecruitStatus status;
    private String selfDescription;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
    @Column(insertable = false, updatable = false)
    private Long projectId;
    @Column(insertable = false, updatable = false)
    private String projectName;

    @ManyToOne
    @JoinColumn(name="accountId")
    private User user;

    @ManyToOne
    @JoinColumn(name="projectId")
    private Project project;

}
