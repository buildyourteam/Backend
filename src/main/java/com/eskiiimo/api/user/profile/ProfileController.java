package com.eskiiimo.api.user.profile;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/profiles", produces = MediaTypes.HAL_JSON_VALUE)
public class ProfileController {


    private final ProfileService ProfileService;

    public ProfileController(ProfileService projectDetailService) {
        this.ProfileService = projectDetailService;
    }


    @GetMapping(value = "/{user_id}")
    public ResponseEntity getProfile(@PathVariable String user_id){
        ProfileDto profileDto = ProfileService.getProfile(user_id);
        ProfileResource profileResource = new ProfileResource(profileDto,user_id);
        profileResource.add(new Link("/docs/index.html#resources-profile-get").withRel("profile"));
        return ResponseEntity.ok(profileResource);
    }

    @PutMapping(value = "/{user_id}")
    public ResponseEntity updateProfile(@PathVariable String user_id,@RequestBody ProfileDto updateData){
        ProfileDto profileDto = ProfileService.updateProfile(user_id,updateData);
        ProfileResource profileResource = new ProfileResource(profileDto,user_id);
        profileResource.add(new Link("/docs/index.html#resources-profile-update").withRel("profile"));
        return ResponseEntity.ok(profileResource);
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
}
