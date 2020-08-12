package com.eskiiimo.web.common.TestFactory.user;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.model.UsersStack;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.web.security.provider.JwtTokenProvider;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.enumtype.UserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestUserFactory {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;


    public User generateUser(int index) {
        List<UsersStack> stacks1 = new ArrayList<UsersStack>();
        stacks1.add(new UsersStack(TechnicalStack.SPRINGBOOT));
        User user = User.builder()
                .userId("user" + index)
                .password(passwordEncoder.encode("testpassword"))
                .grade((long) 1)
                .stacks(stacks1)
                .area("Seoul")
                .userName("UserName" + (3 * index + 1))
                .role(ProjectRole.DEVELOPER)
                .contact("010-1234-5678")
                .introduction("테스트용 가계정" + index)
                .state(UserState.FREE)
                .activate(UserActivate.REGULAR)
                .refreshToken(jwtTokenProvider.createRefreshToken("user" + index, Collections.singletonList("ROLE_USER")))
                .build();
        return this.userRepository.save(user);
    }

    public User generateUser(int index, UserActivate userActivate) {
        List<UsersStack> stacks1 = new ArrayList<UsersStack>();
        stacks1.add(new UsersStack(TechnicalStack.SPRINGBOOT));
        User user = User.builder()
                .userId("user" + index)
                .password(passwordEncoder.encode("testpassword"))
                .grade((long) 1)
                .stacks(stacks1)
                .area("Seoul")
                .userName("UserName" + (3 * index + 1))
                .role(ProjectRole.DEVELOPER)
                .contact("010-1234-5678")
                .introduction("테스트용 가계정" + index)
                .state(UserState.FREE)
                .activate(userActivate)
                .build();
        return this.userRepository.save(user);
    }

    public User generateLeader(int index) {
        List<UsersStack> stacks1 = new ArrayList<UsersStack>();
        stacks1.add(new UsersStack(TechnicalStack.SPRINGBOOT));
        User user = User.builder()
                .userId("testLeader" + (3 * index + 1))
                .password(passwordEncoder.encode("testpassword"))
                .grade((long) 1)
                .stacks(stacks1)
                .area("Seoul")
                .userName("User" + (3 * index + 1))
                .role(ProjectRole.LEADER)
                .state(UserState.FREE)
                .activate(UserActivate.REGULAR)
                .build();
        return this.userRepository.save(user);
    }

    public void generatePeople() {
        IntStream.range(0, 4).forEach(this::generateUser);
    }
}
