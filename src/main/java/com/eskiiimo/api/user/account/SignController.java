package com.eskiiimo.api.user.account;


import com.eskiiimo.api.user.account.exception.CSigninFailedException;
import com.eskiiimo.api.user.account.exception.CUserNotFoundException;
import com.eskiiimo.api.user.account.exception.SignInDto;
import com.eskiiimo.api.user.account.exception.SignUpDto;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/auth")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody SignInDto signInDto) {

        Optional<User> optionalUser = userRepository.findByUserId(signInDto.getUserId());
        if(optionalUser.isEmpty())
            throw new CUserNotFoundException();
        User user = optionalUser.get();
        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword()))
            throw new CSigninFailedException();
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("X-AUTH-TOKEN", jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));

        return new ResponseEntity(header, HttpStatus.OK);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity signin(@RequestBody SignUpDto signUpDto) {

        userRepository.save(User.builder()
                .userId(signUpDto.getUserId())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .userName(signUpDto.getName())
                .userEmail(signUpDto.getUserEmail())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return ResponseEntity.ok().build();
    }
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity userNotFound(HttpServletRequest request, CUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 회원입니다.");
    }

    @ExceptionHandler(CSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity signinFailed(HttpServletRequest request, CSigninFailedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("아이디 또는 비밀번호가 정확하지 않습니다.");
    }
}
