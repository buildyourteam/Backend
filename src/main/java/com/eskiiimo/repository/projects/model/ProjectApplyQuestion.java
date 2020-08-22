package com.eskiiimo.repository.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "questionId")
@Entity
@Table(name = "T_APPLY_QUESTION")
public class ProjectApplyQuestion {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;
    private String question;

    public ProjectApplyQuestion(String question) {
        this.question = question;
    }
}

