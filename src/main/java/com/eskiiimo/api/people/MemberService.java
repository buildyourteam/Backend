package com.eskiiimo.api.people;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    Long save(Member member) {
        return memberRepository.save(member).getMemberId();
    }

    @Transactional(readOnly = true)
    Member getOne(Long user_id) {
        return memberRepository.getOne(user_id);
    }

    @Transactional(readOnly = true)
    List<Member> getMemberList() {
        return memberRepository.findAll();
    }

    Long deleteMember(Long member_id) {
        memberRepository.deleteById(member_id);
        return member_id;
    }

}
