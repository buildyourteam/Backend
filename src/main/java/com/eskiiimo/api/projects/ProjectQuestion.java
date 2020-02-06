package com.eskiiimo.api.projects;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class ProjectQuestion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectQuestionId;
    @Column(name="question")
    private String question;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectQuestionId")
    private List<ProjectAnswer> answers = new ArrayList<ProjectAnswer>();
}
