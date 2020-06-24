package com.eskiiimo.repository.projects.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="projectid")
@Entity
public class ProjectImage {

    @Id
    private Long projectid;
    private String filePath;

    public ProjectImage(Long projectid, String filePath){
        this.projectid = projectid;
        this.filePath = filePath;
    }

}