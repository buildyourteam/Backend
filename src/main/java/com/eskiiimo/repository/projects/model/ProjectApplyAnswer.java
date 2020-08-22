package com.eskiiimo.repository.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "answerId")
@Entity
@Table(name = "T_APPLY_ANSWER")
public class ProjectApplyAnswer {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerId;
    private String answer;

    public ProjectApplyAnswer(String answer) {
        this.answer = answer;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }
}