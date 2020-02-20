package com.eskiiimo.api.projects;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="projectAnswerId")
@Entity
public class ProjectAnswer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectAnswerId;
    private String answer;

}