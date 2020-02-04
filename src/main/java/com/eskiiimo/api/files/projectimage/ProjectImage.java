package com.eskiiimo.api.files.projectimage;


import lombok.*;

import javax.persistence.*;

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
