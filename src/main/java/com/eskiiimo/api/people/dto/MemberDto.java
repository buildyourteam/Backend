package com.eskiiimo.api.people.dto;

import com.eskiiimo.api.people.Member;
import lombok.Getter;

@Getter
public class MemberDto {
    private String userId;
    private String userPassword;
    private String userEmail;
    private String userName;

    public Member toEntity(MemberDto memberDto) {
        return Member.builder()
                .userId(memberDto.userId)
                .userPassword(memberDto.userPassword)
                .userEmail(memberDto.userEmail)
                .userName(memberDto.userName)
                .build();
    }
}

