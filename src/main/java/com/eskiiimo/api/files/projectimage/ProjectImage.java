package com.eskiiimo.api.files.projectimage;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="id")
@Entity
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long projectid;
    private String filePath;

    public ProjectImage(Long projectid, String filePath){
        this.projectid = projectid;
        this.filePath = filePath;
    }

}
