package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

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
}
