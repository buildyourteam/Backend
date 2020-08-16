package com.eskiiimo.web.security.controller;


import com.eskiiimo.web.security.request.RefreshRequest;
import com.eskiiimo.web.security.request.SignInRequest;
import com.eskiiimo.web.security.request.SignUpRequest;
import com.eskiiimo.web.security.response.RefreshResponse;
import com.eskiiimo.web.security.response.SignInResponse;
import com.eskiiimo.web.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 인증 컨트롤러
 *
 * @author always0ne
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 토큰 발급받기
     *
     * @param signInRequest 사용자 ID, 비밀번호
     * @return accessToken
     */
    @PostMapping(value = "/signin")
    public SignInResponse signin(
            @RequestBody SignInRequest signInRequest
    ) {
        return this.authService.signIn(signInRequest);
    }

    /**
     * 회원 가입하기
     *
     * @param signUpRequest 사용자 ID, 비밀번호, 이름
     * @return accessToken
     */
    @PostMapping(value = "/signup")
    public SignInResponse signup(
            @RequestBody SignUpRequest signUpRequest
    ) {
        return this.authService.signUp(signUpRequest);
    }

    /**
     * 아이디 중복 체크하기
     *
     * @param checkId 중복확인할  ID
     * @return 사용가능 여부
     */
    @PostMapping(value = "/idcheck/{checkId}")
    public String canUseThisId(
            @PathVariable String checkId
    ) {
        this.authService.idCheck(checkId);
        return "사용가능한 아이디입니다.";
    }

    /**
     * RefreshToken 으로 AccessToken 재발급 받기
     *
     * @param refreshRequest 토큰 갱신 요청
     * @return AccessToken
     */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public RefreshResponse getNewAccessToken(
            @RequestBody RefreshRequest refreshRequest
    ) {
        return this.authService.refreshAccessToken(refreshRequest);
    }
}
