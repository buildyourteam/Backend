package com.eskiiimo.web.security.service;

import com.eskiiimo.repository.security.dto.SignInDto;
import com.eskiiimo.repository.security.dto.SignUpDto;
import com.eskiiimo.repository.person.model.Person;
import com.eskiiimo.repository.person.repository.PersonRepository;
import com.eskiiimo.web.security.exception.CSigninFailedException;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;


    public boolean signup(SignUpDto signupDto) {
        personRepository.save(Person.builder()
                .personId(signupDto.getUserId())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .personName(signupDto.getName())
                .personEmail(signupDto.getUserEmail())
                .personRoles(Collections.singletonList("ROLE_USER"))
                .build());
        return true;
    }

    public Person signin(SignInDto signInDto) {
        Optional<Person> optionalPerson = personRepository.findByPersonId(signInDto.getUserId());
        if(optionalPerson.isEmpty())
            throw new CUserNotFoundException();
        Person person = optionalPerson.get();
        if (!passwordEncoder.matches(signInDto.getPassword(), person.getPassword()))
            throw new CSigninFailedException();
        return person;
    }

    public boolean idCheck(String checkId) {
        Optional<Person> optionalPerson = personRepository.findByPersonId(checkId);
        if(optionalPerson.isEmpty())
            return true;
        else
            return false;
    }
}
