package com.eskiiimo.web.common.TestFactory.user;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.web.security.provider.JwtTokenProvider;
import com.eskiiimo.web.security.request.SignUpRequest;
import com.eskiiimo.web.security.service.AuthService;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import com.eskiiimo.web.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestUserFactory {

    @Autowired
    AuthService authService;
    @Autowired
    ProfileService profileService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;


    public User generateUser(int index) {
        authService.signUp(generateSignUpRequest(index));
        UpdateProfileRequest updateProfileRequest = generateUpdateProfileRequest(index, "테스트용 가계정" + index, ProjectRole.DEVELOPER);
        profileService.updateProfile("user" + index, "user" + index, updateProfileRequest);
        return userRepository.findByUserIdAndActivate("user" + index, UserActivate.REGULAR).get();
    }

    public void generateLeader(int index) {
        authService.signUp(generateSignUpRequest(index));
        UpdateProfileRequest updateProfileRequest = generateUpdateProfileRequest(index, "테스트용 가계정" + index, ProjectRole.LEADER);
        profileService.updateProfile("user" + index, "user" + index, updateProfileRequest);
    }

    public User generateBlockedUser(int index) {
        generateUser(index);
        User user = userRepository.findByUserIdAndActivate("user" + index, UserActivate.REGULAR).get();
        authService.blockUser("user"+index);
        return user;
    }

    public void generatePeople() {
        IntStream.range(0, 4).forEach(this::generateUser);
    }

    public SignUpRequest generateSignUpRequest(int index) {

        return SignUpRequest.builder()
                .name("user" + index)
                .password("password")
                .userEmail("test@email.com")
                .userId("user" + index)
                .build();
    }

    public UpdateProfileRequest generateUpdateProfileRequest(int index, String introduction, ProjectRole role) {
        List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
        stacks.add(TechnicalStack.SPRINGBOOT);

        return UpdateProfileRequest.builder()
                .area("Seoul")
                .contact("010-1234-5678")
                .introduction(introduction)
                .role(role)
                .stacks(stacks)
                .userName("user" + index)
                .build();
    }
}
