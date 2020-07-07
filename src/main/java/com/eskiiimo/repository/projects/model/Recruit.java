package com.eskiiimo.repository.projects.model;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.web.projects.enumtype.SuggestStatus;
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
    private SuggestStatus suggestStatus;

    private String selfDescription;

    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;

    @Column(insertable = false, updatable = false)
    private Long projectId;

    @Column(insertable = false, updatable = false)
    private String projectName;

    @ManyToOne
    @JoinColumn(name="accountId")
    private Person person;

    @ManyToOne
    @JoinColumn(name="projectId")
    private Project project;

}
