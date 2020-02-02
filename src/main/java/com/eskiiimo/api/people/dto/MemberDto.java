package com.eskiiimo.api.people.dto;

import lombok.Getter;

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
