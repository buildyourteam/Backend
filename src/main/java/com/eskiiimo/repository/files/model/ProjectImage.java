package com.eskiiimo.repository.files.model;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "imageId")
@Entity
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private Long projectId;
    private String filePath;

    @Builder
    public ProjectImage(Long projectId, String filePath) {
        this.projectId = projectId;
        this.filePath = filePath;
    }

    public void updateProjectImage(Long projectId, String filePath) {
        this.projectId = projectId;
        this.filePath = filePath;
    }

}