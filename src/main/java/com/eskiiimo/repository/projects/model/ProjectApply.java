package com.eskiiimo.repository.projects.model;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import com.eskiiimo.repository.user.model.User;
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
@Table(name = "T_APPLY")
public class ProjectApply {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long applyId;
    @Enumerated(EnumType.STRING)
    private ProjectApplyState state;
    private String introduction;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="applyId")
    private List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
    @ManyToOne
    @JoinColumn(name="accountId")
    private User user;
}