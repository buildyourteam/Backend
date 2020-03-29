package com.eskiiimo.api.people;

import com.eskiiimo.api.people.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberInfoRepository memberInfoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    Long save(MemberInfo memberInfo) {
        return memberInfoRepository.save(memberInfo).getMemberId();
    }

    @Transactional(readOnly = true)
    List<MemberInfo> getMemberList() {
        return memberInfoRepository.findAll();
    }

    void deleteMember(Long member_id) {
        memberInfoRepository.deleteById(member_id);
    }

    public Optional<MemberInfo> findById(Long member_id) {
        Optional<MemberInfo> memberInfoOptional = memberInfoRepository.findById(member_id);
        if(memberInfoOptional.isEmpty()) {
            return null;
        } else {
            return memberInfoOptional;
        }
    }
}
