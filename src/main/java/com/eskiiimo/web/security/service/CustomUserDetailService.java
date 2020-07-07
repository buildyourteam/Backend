package com.eskiiimo.web.security.service;

import com.eskiiimo.repository.person.repository.PersonRepository;
import com.eskiiimo.web.security.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final PersonRepository personRepository;

    public UserDetails loadUserByUsername(String personPk) {
        return personRepository.findByPersonId(personPk).orElseThrow(CUserNotFoundException::new);
    }
}
