package com.eskiiimo.api.security;


import com.eskiiimo.api.security.exception.CSigninFailedException;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/auth")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/signin")
    public ResponseEntity signin(@RequestParam String id,
                                         @RequestParam String password) {

        User user = userRepository.findByUserId(id).orElseThrow(CSigninFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CSigninFailedException();
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("AUTHCODE","xxxxxxx");
        header.add("TOKEN", jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));

        return new ResponseEntity(header, HttpStatus.OK);
    }

    @GetMapping(value = "/signup")
    public ResponseEntity signin(@RequestParam String id,
                                 @RequestParam String password,
                                 @RequestParam String name) {

        userRepository.save(User.builder()
                .userId(id)
                .password(passwordEncoder.encode(password))
                .userName(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return ResponseEntity.ok().body(id);
    }
}
