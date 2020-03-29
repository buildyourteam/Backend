package com.eskiiimo.api.people;

import com.eskiiimo.api.people.dto.MemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberInfo, Long> {

}

