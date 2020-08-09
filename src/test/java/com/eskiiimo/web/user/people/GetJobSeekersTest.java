package com.eskiiimo.web.user.people;

import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("피플")
public class GetJobSeekersTest extends BaseControllerTest {
    @Test
    @DisplayName("팀을 구하는 사람들")
    void getJobSeekersSuccess() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
        )
                .andDo(print());
    }

    @Test
    @DisplayName("팀을 구하는 사람들_레벨")
    void getJobSeekers_gradeSuccess() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade", "1")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("팀을 구하는 사람들_역할")
    void getJobSeekers_roleSuccess() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("role", "DEVELOPER")
        )
                .andDo(print());
    }

    @Test
    @DisplayName("팀을 구하는 사람들_지역")
    void getJobSeekers_areaSuccess() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("area", "Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print());
    }

    @Test
    @DisplayName("팀을 구하는 사람들_레벨_역할")
    void getJobSeekers_gradeAndRoleSuccess() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade", "1")
                .param("role", "DEVELOPER")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("팀을 구하는 사람들_레벨_지역")
    void getJobSeekers_gradeAndAreaSuccess() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade", "1")
                .param("area", "Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print());
    }

    @Test
    @DisplayName("팀을 구하는 사람들_역할_지역")
    void getJobSeekers_RoleAndAreaSuccess() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("role", "DEVELOPER")
                .param("area", "Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print());
    }

    @Test
    @DisplayName("팀을 구하는 사람들_레벨_역할_지역")
    void getJobSeekers_LevelAndRoleAndAreaSuccess() throws Exception {
        IntStream.range(0, 4).forEach(testUserFactory::generateLeader);
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "3")
                .param("grade", "1")
                .param("role", "LEADER")
                .param("area", "Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print())
                .andDo(document("get-people",
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 데이터 수"),
                                parameterWithName("grade").description("유저 레벨 필터"),
                                parameterWithName("role").description("유저 역할 필터"),
                                parameterWithName("area").description("유저 활동지역 필터")
                        )
                ));
    }
}