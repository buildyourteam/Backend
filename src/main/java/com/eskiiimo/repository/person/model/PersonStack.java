package com.eskiiimo.repository.person.model;

import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of="stackId")
@Entity
@Table(name = "T_PERSON_STACK")
public class PersonStack {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long stackId;

    @Enumerated(EnumType.STRING)
    TechnicalStack stack;

    public PersonStack(TechnicalStack stack){
        this.stack =stack;
    }
}
