package com.eskiiimo.repository.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of="questionId")
@Entity
@Table(name = "T_APPLY_QUESTION")
public class ProjectApplyQuestion {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long questionId;
    private String question;
}

