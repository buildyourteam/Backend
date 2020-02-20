package com.eskiiimo.api.user.people;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PeopleController {

    // 팀을 구하고 있는 사람 리스트 반환 (컨트롤러 쪼개기)
    @GetMapping("/api/people")
    public ResponseEntity getJobSeekers() {
        return  null;
    }
}
