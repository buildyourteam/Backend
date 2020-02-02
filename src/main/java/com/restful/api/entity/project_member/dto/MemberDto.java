package com.eskiiimo.api.dto;

import com.eskiiimo.api.people.Member;
import lombok.Getter;

/*
 * MemberDto를 따로 만들어서 엔터티에 바로 접근하지 않도록 하기 (제안)
 */

@Getter
public class MemberDto {
    private Long id;
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userName;

    // 제안 사항
//    public Member toEntity() {
//        return new Member(id, userId, userPassword, userEmail, userName);
//    }
}
