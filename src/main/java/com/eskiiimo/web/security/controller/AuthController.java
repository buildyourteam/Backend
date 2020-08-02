package com.eskiiimo.web.security.controller;


import com.eskiiimo.web.security.service.AuthService;
import com.eskiiimo.web.security.provider.JwtTokenProvider;
import com.eskiiimo.web.security.request.SignInRequest;
import com.eskiiimo.web.security.request.SignUpRequest;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/signin")
    public ResponseEntity signin(
            @RequestBody SignInRequest signInRequest
    ) {
        User user = authService.signin(signInRequest);
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("authtoken", jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));

        return new ResponseEntity(header, HttpStatus.OK);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity signup(
            @RequestBody SignUpRequest signupRequest
    ) {
        if(authService.signup(signupRequest))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


    }
    @PostMapping(value = "/idcheck/{checkId}")
    public ResponseEntity canUseThisId(
            @PathVariable String checkId
    ) {
        if(authService.idCheck(checkId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
