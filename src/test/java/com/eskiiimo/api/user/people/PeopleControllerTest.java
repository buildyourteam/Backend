package com.eskiiimo.api.user.people;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class PeopleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Test
    void getJobSeekers() throws Exception {
        IntStream.range(0,30).forEach(i -> {
            this.generatePeople(i);
        });

        // When & Then
        this.mockMvc.perform(get("/api/people")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "user_name,DESC")
        )
                .andDo(print());
    }

    public void generatePeople(int index){
        User user1 = User.builder()
                .userId("tester"+(3*index+1))
                .level((long)1)
                .stack(TechnicalStack.SPRINGBOOT)
                .area("Seoul")
                .userName("User"+(3*index+1))
                .role(ProjectRole.LEADER)
                .build();
        User user2 = User.builder()
                .userId("tester"+(3*index+2))
                .level((long)2)
                .stack(TechnicalStack.DJANGO)
                .area("Busan")
                .userName("User"+(3*index+2))
                .role(ProjectRole.DEVELOPER)

                .build();
        User user3 = User.builder()
                .userId("tester"+(3*index+3))
                .level((long)3)
                .stack(TechnicalStack.SPRINGBOOT)
                .area("Daegu")
                .userName("User"+(3*index-+3))
                .role(ProjectRole.DEVELOPER)
                .build();
        this.userRepository.save(user1);
        this.userRepository.save(user2);
        this.userRepository.save(user3);
    }
}