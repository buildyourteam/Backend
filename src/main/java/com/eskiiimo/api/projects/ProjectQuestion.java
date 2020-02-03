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
@EqualsAndHashCode(of="projectQuestionId")
@Entity
public class ProjectQuestion {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectQuestionId;
    @Column(name="question")
    private String question;
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectQuestionId")
    private List<ProjectAnswer> answers = new ArrayList<ProjectAnswer>();
}
