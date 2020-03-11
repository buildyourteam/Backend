package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.error.exception.UserNotFoundException;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectRepository;
import com.eskiiimo.api.projects.Status;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ModelMapper modelMapper;

    @Transactional
    public ProfileDto getProfile(String user_id) {
        User profile =  userRepository.findByUserId(user_id)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProfileDto profileDto = profile.toProfileDto();
        return profileDto;
    }

    @Transactional
    public ProfileDto updateProfile(String user_id, ProfileDto updateData) {
        User profile =  userRepository.findByUserId(user_id)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        updateData.updateProfile(profile);
        this.userRepository.save(profile);
        return updateData;
    }

    public Page<Project> getRunning(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectStatus_UserIdAndProjectStatus_Status(user_id, Status.RUNNING.toString(), pageable);
        return page;
    }

    public Page<Project> getEnded(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectStatus_UserIdAndProjectStatus_Status(user_id, Status.ENDED.toString(), pageable);
        return page;
    }

    public Page<Project> getPlanner(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectStatus_UserIdAndProjectStatus_Plan(user_id, Boolean.TRUE, pageable);
        return page;
    }
}
