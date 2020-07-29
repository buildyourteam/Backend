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
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String userId;
    private String filePath;

    @Builder
    public ProfileImage(String userId, String filePath) {
        this.userId = userId;
        this.filePath = filePath;
    }

    public void updateProfileImage(String userId, String filePath) {
        this.userId = userId;
        this.filePath = filePath;
    }

}