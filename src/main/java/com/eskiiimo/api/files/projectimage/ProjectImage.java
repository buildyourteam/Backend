package com.eskiiimo.api.files.projectimage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class ProjectImage {

    @Id
    @GeneratedValue
    private Long id;
    private Long projectid;
    private String filePath;

    public ProjectImage(Long projectid, String filePath){
        this.projectid = projectid;
        this.filePath = filePath;
    }
}
