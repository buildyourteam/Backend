package com.eskiiimo.repository.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="answerId")
@Entity
@Table(name = "T_APPLY_ANSWER")
public class ProjectApplyAnswer {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long answerId;
    private String answer;

}