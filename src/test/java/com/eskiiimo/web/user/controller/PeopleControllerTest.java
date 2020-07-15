package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
class PeopleControllerTest extends BaseControllerTest {

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("팀을 구하는 사람들")
    @Transactional
    void getJobSeekers() throws Exception {
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
    @Transactional
    void getJobSeekers_grade() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade","1")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andDo(print())
;
    }
    @Test
    @DisplayName("팀을 구하는 사람들_역할")
    @Transactional
    void getJobSeekers_role() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("role","DEVELOPER")
        )
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_지역")
    @Transactional
    void getJobSeekers_area() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("area","Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_레벨_역할")
    @Transactional
    void getJobSeekers_gradeAndRole() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade","1")
                .param("role","DEVELOPER")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_레벨_지역")
    @Transactional
    void getJobSeekers_gradeAndArea() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade","1")
                .param("area","Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_역할_지역")
    @Transactional
    void getJobSeekers_RoleAndArea() throws Exception {
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("role","DEVELOPER")
                .param("area","Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_레벨_역할_지역")
    @Transactional
    void getJobSeekers_LevelAndRoleAndArea() throws Exception {
        IntStream.range(0,4).forEach(testUserFactory::generateLeader);
        testUserFactory.generatePeople();

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "3")
                .param("grade","1")
                .param("role","LEADER")
                .param("area","Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleList[0].grade").value(1))
                .andExpect(jsonPath("_embedded.peopleList[0].area").value("Seoul"))
                .andDo(print())
                .andDo(document("get-people",
                        links(
                                linkWithRel("self").description("Self 링크"),
                                linkWithRel("next").description("다음 리스트"),
                                linkWithRel("last").description("이전 리스트"),
                                linkWithRel("first").description("리스트 첫번째 페이지")
                        ),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 데이터 수"),
                                parameterWithName("grade").description("유저 레벨 필터"),
                                parameterWithName("role").description("유저 역할 필터"),
                                parameterWithName("area").description("유저 활동지역 필터")
                        ),
                        responseFields(

                                fieldWithPath("_embedded.peopleList[].userId").description("유저의 아이디"),
                                fieldWithPath("_embedded.peopleList[].userName").description("유저의 이름"),
                                fieldWithPath("_embedded.peopleList[].stacks").description("유저의 기술 스택"),
                                fieldWithPath("_embedded.peopleList[].area").description("유저의 활동지역"),
                                fieldWithPath("_embedded.peopleList[].grade").description("유저의 레벨"),
                                fieldWithPath("_embedded.peopleList[].role").description("유저가 관심 있는 분야"),
                                fieldWithPath("_links.self.href").description("Self 링크"),
                                fieldWithPath("_links.first.href").description("리스트 첫번째 페이지"),
                                fieldWithPath("_links.next.href").description("다음 리스트"),
                                fieldWithPath("_links.last.href").description("이전 리스트"),
                                fieldWithPath("page.size").description("페이지당 데이터 수"),
                                fieldWithPath("page.totalElements").description("총 데이터 수"),
                                fieldWithPath("page.totalPages").description("총 페이지 수"),
                                fieldWithPath("page.number").description("현재 페이지")

                        )
                ));
    }
}