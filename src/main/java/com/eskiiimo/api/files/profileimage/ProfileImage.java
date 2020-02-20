package com.eskiiimo.api.files.profileimage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;


@NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of="memberid")
@Entity
public class ProfileImage {

    @Id
    private Long memberid;
    private String filePath;

    public ProfileImage(Long memberid, String filePath){
        this.memberid = memberid;
        this.filePath = filePath;
    }

}