package com.eskiiimo.repository.files.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="projectid")
@Entity
@Table(name = "T_PROJECT_IMAGE")
public class ProjectImage {

    @Id
    private Long projectid;
    private String filePath;

    public ProjectImage(Long projectid, String filePath){
        this.projectid = projectid;
        this.filePath = filePath;
    }

}