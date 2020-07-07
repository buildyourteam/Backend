package com.eskiiimo.repository.projects.model;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.ProjectApplyStatus;
import com.eskiiimo.repository.person.model.Person;
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
@Table(name = "T_PROJECT_APPLY")
public class ProjectApply {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long applyId;
    @Enumerated(EnumType.STRING)
    private ProjectApplyStatus projectApplyStatus;
    private String selfDescription;
    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="applyId")
    @Builder.Default
    private List<ProjectApplyAnswer> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="accountId")
    private Person person;
}
