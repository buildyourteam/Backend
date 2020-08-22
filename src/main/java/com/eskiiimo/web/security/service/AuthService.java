package com.eskiiimo.web.security.service;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.security.exception.IdAlreadyExistsException;
import com.eskiiimo.web.security.exception.NotRegularUserException;
import com.eskiiimo.web.security.exception.SigninFailedException;
import com.eskiiimo.web.security.provider.JwtTokenProvider;
import com.eskiiimo.web.security.request.RefreshRequest;
import com.eskiiimo.web.security.request.SignInRequest;
import com.eskiiimo.web.security.request.SignUpRequest;
import com.eskiiimo.web.security.response.RefreshResponse;
import com.eskiiimo.web.security.response.SignInResponse;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 회원 인증 컨트롤러
 *
 * @author always0ne
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입 하기
     * 회원가입과 동시에 인증토큰 발급
     *
     * @param signUpRequest 회원가입 요청
     * @return accessToken
     */
    @Transactional
    public SignInResponse signUp(SignUpRequest signUpRequest) {
        User user = userRepository.save(
                new User(
                        signUpRequest.getUserId(),
                        passwordEncoder.encode(signUpRequest.getPassword()),
                        signUpRequest.getName(),
                        signUpRequest.getUserEmail(),
                        jwtTokenProvider.createRefreshToken(signUpRequest.getUserId(), Collections.singletonList("ROLE_USER"))
                )
        );

        return SignInResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getUserId(), user.getRoles()))
                .refreshToken(user.getRefreshToken())
                .build();
    }

    /**
     * 인증토큰 발급받기
     * 새로 로그인 할 때마다 RefreshToken 이 갱신된다.
     *
     * @param signInRequest 토큰발급 요청
     * @return accessToken
     * @throws SigninFailedException 회원가입이 되어있지 않거나 잠긴 계정입니다.
     */
    @Transactional
    public SignInResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByUserIdAndActivate(signInRequest.getUserId(), UserActivate.REGULAR)
                .orElseThrow(SigninFailedException::new);
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword()))
            throw new SigninFailedException();

        user.updateRefreshToken(jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRoles()));

        return SignInResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getUserId(), user.getRoles()))
                .refreshToken(user.getRefreshToken())
                .build();
    }

    /**
     * 중복 아이디 체크
     *
     * @param checkId 사용자 ID
     * @throws IdAlreadyExistsException 이미 사용중인 아이디입니다.
     */
    @Transactional(readOnly = true)
    public void idCheck(String checkId) {
        if (this.userRepository.findByUserIdAndActivateIsNot(checkId, UserActivate.DELETED).isPresent())
            throw new IdAlreadyExistsException(checkId);
    }

    /**
     * RefreshToken 으로 AccessToken 재발급
     *
     * @param refreshRequest AccessToken, RefreshToken
     * @return AccessToken
     */
    @Transactional
    public RefreshResponse refreshAccessToken(RefreshRequest refreshRequest) {
        String refreshId = jwtTokenProvider.getUserId(jwtTokenProvider.getClaimsFromToken(refreshRequest.getRefreshToken()));
        User user = userRepository.findByUserIdAndActivateAndRefreshToken(refreshId, UserActivate.REGULAR, refreshRequest.getRefreshToken())
                .orElseThrow(NotRegularUserException::new);

        return RefreshResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getUserId(), user.getRoles()))
                .build();
    }

    /**
     * 계정 제한하기
     * Admin Api로 옮겨갈 예정
     *
     * @param userId 사용자 ID
     */
    @Transactional
    public void blockUser(String userId) {
        User user = userRepository.findByUserIdAndActivate(userId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.blockUser();
    }
}
