package com.eskiiimo.api.user.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AccountController {

    @PostMapping(value = "/login")
    public ResponseEntity login(){
        return null;
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logoutEskiiimo(){
        return null;
    }

    @DeleteMapping("/leave")
    public void leaveUser(){

    }
}
