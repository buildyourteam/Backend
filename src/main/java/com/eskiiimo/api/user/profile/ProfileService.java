package com.eskiiimo.api.user.profile;

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
        Optional<User> optionalUser = this.userRepository.findByUserId(user_id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User profile = optionalUser.get();
        ProfileDto profileDto = profile.toProfileDto();
        return profileDto;
    }

    @Transactional
    public ProfileDto updateProfile(String user_id, ProfileDto updateData) {
        Optional<User> optionalUser = this.userRepository.findByUserId(user_id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User profile = optionalUser.get();
        updateData.updateProfile(profile);
        this.userRepository.save(profile);
        return updateData;
    }

    public Page<Project> getRunning(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectStatus_UserIdAndProjectStatus_Status(user_id, Status.RUNNING.toString(), pageable);
        return page;
    }
}
