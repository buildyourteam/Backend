package com.eskiiimo.repository.projects.model;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.repository.person.model.Person;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="projectPersonId")
@Entity
@Table(name = "T_PROJECT_PERSON")
public class ProjectPerson {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectPersonId;

    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;

    @Enumerated(EnumType.STRING)
    private TechnicalStack stack;

    @Lob
    private String selfDescription;

    Boolean hide;

    @ManyToOne
    @JoinColumn(name="accountId")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;


    public void setProject(Project project){
        this.project = project;
        if(!project.getProjectPersons().contains(this))
            this.project.addPerson(this);
    }
}