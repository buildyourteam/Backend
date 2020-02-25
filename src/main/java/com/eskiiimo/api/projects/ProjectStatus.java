package com.eskiiimo.api.projects;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="projectStatusId")
@Entity
public class ProjectStatus {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long projectStatusId;

    private String userId;
    private String status;
    private Boolean plan;



}
