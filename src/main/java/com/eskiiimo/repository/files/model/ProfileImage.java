package com.eskiiimo.repository.files.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="imageId")
@Entity @Builder
@Table(name = "T_PROFILE_IMAGE")
public class ProfileImage {

    @Id
    @GeneratedValue()
    private Long imageId;
    private String personId;
    private String filePath;

    public ProfileImage(Long imageId,String personId, String filePath){
        this.imageId = imageId;
        this.personId = personId;
        this.filePath = filePath;
    }

}