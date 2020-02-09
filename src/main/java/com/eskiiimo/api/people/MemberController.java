package com.eskiiimo.api.people;

import com.eskiiimo.api.people.dto.MemberDto;
import com.eskiiimo.api.people.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile/{user_id}")
public class MemberController {

    private final MemberService memberService;

    // 팀을 구하고 있는 사람 리스트 반환 (컨트롤러 쪼개기)
//    @GetMapping("/api/people")
//    public List<PeopleListDto> getJobSeekers() {
//
//    }

    //
    @GetMapping
    public ProfileDto getProfile(@PathVariable(value = "user_id") Long user_id, Model model) {
        return null;
    }

    // 사용자가 참여 중인 프로젝트 리스트 가져오기
//    @GetMapping("/running")
//    public List<ProjectsListDto> getRunningProjects(@PathVariable(value = "user_id") Long user_id, Model model) {
//
//    }
    // 사용자가 참여했던 프로젝트 리스트 가져오기
//    @GetMapping("/ended")
//    public List<ProjectsListDto> getEndedProjects(@PathVariable(value = "user_id") Long user_id, Model model) {
//
//    }

    // 사용자가 기획한 프로젝트 리스트 가져오기
//    public List<ProjectsListDto> getMyPlanProjects(@PathVariable(value = "user_id") Long user_id, Model model) {
//
//    }

    // 프로필 변경
    @PostMapping("/info")
    public MemberDto updateProfile(@PathVariable(value = "user_id") Long user_id) {

        return null;
    }

    // 계정 삭제
    @DeleteMapping
    public void deleteAccount(@PathVariable(value = "user_id") Long user_id) {
        memberService.deleteMember(user_id);
    }

}

