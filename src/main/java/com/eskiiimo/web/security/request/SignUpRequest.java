package com.eskiiimo.web.security.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청
 *
 * @author always0ne
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {
    /**
     * 사용자 ID
     */
    private String userId;
    /**
     * 사용자 이메일
     */
    private String userEmail;
    /**
     * 사용자 비밀번호
     */
    private String password;
    /**
     * 사용자 이름
     */
    private String name;
}
