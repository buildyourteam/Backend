package com.eskiiimo.web.security.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {
    String userId;
    String userEmail;
    String password;
    String name;
}
