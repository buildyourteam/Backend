package com.eskiiimo.web.security.controller;


import com.eskiiimo.web.security.service.AuthService;
import com.eskiiimo.web.security.provider.JwtTokenProvider;
import com.eskiiimo.repository.security.dto.SignInDto;
import com.eskiiimo.repository.security.dto.SignUpDto;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.repository.person.repository.PersonRepository;
import com.eskiiimo.web.security.exception.CSigninFailedException;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/auth")
public class AuthController {

    private final PersonRepository personRepository;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/signin")
    public ResponseEntity signin(@RequestBody SignInDto signInDto) {
        Person person = authService.signin(signInDto);
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("authtoken", jwtTokenProvider.createToken(person.getUsername(), person.getPersonRoles()));

        return new ResponseEntity(header, HttpStatus.OK);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity signup(@RequestBody SignUpDto signupDto) {
        if(authService.signup(signupDto))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


    }
    @PostMapping(value = "/idcheck/{checkId}")
    public ResponseEntity canUseThisId(@PathVariable String checkId) {

        if(authService.idCheck(checkId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity personNotFound(HttpServletRequest request, CUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 회원입니다.");
    }

    @ExceptionHandler(CSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity signinFailed(HttpServletRequest request, CSigninFailedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("아이디 또는 비밀번호가 정확하지 않습니다.");
    }
}
