package com.eskiiimo.api.user.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SignInDto {
    String userId;
    String password;
}
