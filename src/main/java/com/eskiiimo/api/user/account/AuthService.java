package com.eskiiimo.api.user.account;

import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import com.eskiiimo.api.user.account.exception.CSigninFailedException;
import com.eskiiimo.api.user.account.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean signup(SignUpDto signupDto) {
        userRepository.save(User.builder()
                .userId(signupDto.getUserId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .userName(signupDto.getName())
                .userEmail(signupDto.getUserEmail())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return true;
    }

    public User signin(SignInDto signInDto) {
        Optional<User> optionalUser = userRepository.findByUserId(signInDto.getUserId());
        if(optionalUser.isEmpty())
            throw new CUserNotFoundException();
        User user = optionalUser.get();
        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword()))
            throw new CSigninFailedException();
        return user;
    }

    public boolean idCheck(String checkId) {
        Optional<User> optionalUser = userRepository.findByUserId(checkId);
        if(optionalUser.isEmpty())
            return true;
        else
            return false;
    }
}
