package com.eskiiimo.repository.files.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="imageId")
@Entity @Builder
public class ProfileImage {

    @Id
    @GeneratedValue()
    private Long imageId;
    private String userId;
    private String filePath;

    public ProfileImage(Long imageId,String userId, String filePath){
        this.imageId = imageId;
        this.userId = userId;
        this.filePath = filePath;
    }

}