package com.eskiiimo.api.files.profileimage;

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
public class ProfileImage {

    @Id
    @GeneratedValue
    private Long id;
    private Long memberid;
    private String filePath;

    public ProfileImage(Long memberid, String filePath){
        this.memberid = memberid;
        this.filePath = filePath;
    }

}