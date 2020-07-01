package com.eskiiimo.repository.projects.model;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="recruitId")
@Entity
@Table(name = "T_RECRUIT")
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
