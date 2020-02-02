package com.eskiiimo.api.people;

import com.eskiiimo.api.people.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

//    Long save(MemberDto memberDto) {
//    }
}
