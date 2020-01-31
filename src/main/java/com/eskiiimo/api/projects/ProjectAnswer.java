package com.restful.api.projects;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class ProjectAnswer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectAnswerId;
    private String answer;

}
