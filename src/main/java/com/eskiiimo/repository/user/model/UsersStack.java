package com.eskiiimo.repository.user.model;

import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@EqualsAndHashCode(of="stackId")
@Entity
@Table(name = "T_USERS_STACK")
public class UsersStack {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long stackId;

    @Enumerated(EnumType.STRING)
    TechnicalStack stack;

    public UsersStack(TechnicalStack stack){
        this.stack =stack;
    }
}
