package com.eskiiimo.api.people;

import com.eskiiimo.api.people.dto.MemberDto;
import com.eskiiimo.api.people.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 사용자들에 대한 정보 리스트로 불러오기
    @GetMapping("/")
    public void memberList() {

    }

    // 팀을 구하고 있는 사람 리스트 반환
//    @GetMapping("/api/people")
//    public List<PeopleListDto> getJobSeekers() {
//
//    }

    //
    @GetMapping("/api/profile/{user_id}")
    public ProfileDto getProfile(@PathVariable(value = "user_id") Long user_id, Model model) {
        return null;
    }

    // 사용자가 참여 중인 프로젝트 리스트 가져오기
//    @GetMapping("/api/profile/{user_id}/running")
//    public List<ProjectsListDto> getRunningProjects(@PathVariable(value = "user_id") Long user_id, Model model) {
//
//    }
    // 사용자가 참여했던 프로젝트 리스트 가져오기
//    @GetMapping("/api/profile/{user_id}/ended")
//    public List<ProjectsListDto> getEndedProjects(@PathVariable(value = "user_id") Long user_id, Model model) {
//
//    }

    // 사용자가 기획한 프로젝트 리스트 가져오기
//    public List<ProjectsListDto> getMyPlanProjects(@PathVariable(value = "user_id") Long user_id, Model model) {
//
//    }

    // 프로필 변경
    @PostMapping("/api/profile/{user_id}/info")
    public MemberDto updateProfile(@PathVariable(value = "user_id") Long user_id) {

        return null;
    }

    // 계정 삭제
    @DeleteMapping("/api/profile/{user_id}")
    public void deleteAccount(@PathVariable(value = "user_id") Long user_id) {
    }


}

