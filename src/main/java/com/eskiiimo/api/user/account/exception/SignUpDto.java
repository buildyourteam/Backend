package com.eskiiimo.api.user.account.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignUpDto {
    String userId;
    String userEmail;
    String password;
    String name;
}
