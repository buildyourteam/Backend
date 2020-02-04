package com.eskiiimo.api.files.profileimage;

import lombok.*;

import javax.persistence.*;


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
