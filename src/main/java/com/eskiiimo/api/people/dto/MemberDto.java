package com.eskiiimo.api.people.dto;

import lombok.Getter;

@Getter
public class MemberDto {
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userName;

    // 제안 사항 (작업하기)
//    public Member toEntity() {
//        return new Member(userId, userPassword, userEmail, userName);
//    }
}
