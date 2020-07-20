package com.eskiiimo.web.common.TestFactory.user;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.model.UsersStack;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestUserFactory {

    @Autowired
    UserRepository userRepository;

    public User generateUser(int index) {
        List<UsersStack> stacks1 = new ArrayList<UsersStack>();
        stacks1.add(new UsersStack(TechnicalStack.SPRINGBOOT));
        User user = User.builder()
                .userId("user" + index)
                .password("testpassword")
                .grade((long) 1)
                .stacks(stacks1)
                .area("Seoul")
                .userName("UserName" + (3 * index + 1))
                .role(ProjectRole.DEVELOPER)
                .contact("010-1234-5678")
                .introduction("테스트용 가계정" + index)
                .build();
        return this.userRepository.save(user);
    }

    public User generateLeader(int index) {
        List<UsersStack> stacks1 = new ArrayList<UsersStack>();
        stacks1.add(new UsersStack(TechnicalStack.SPRINGBOOT));
        User user = User.builder()
                .userId("testLeader" + (3 * index + 1))
                .password("testpassword")
                .grade((long) 1)
                .stacks(stacks1)
                .area("Seoul")
                .userName("User" + (3 * index + 1))
                .role(ProjectRole.LEADER)
                .build();
        return this.userRepository.save(user);
    }

    public void generatePeople() {
        IntStream.range(0, 4).forEach(this::generateUser);
    }
}
