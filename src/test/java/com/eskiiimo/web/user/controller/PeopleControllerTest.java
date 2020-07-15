package com.eskiiimo.web.user.controller;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.repository.user.model.UsersStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        IntStream.range(35,40).forEach(i -> {
            this.generatePeople(i);
        });

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
        IntStream.range(5,9).forEach(i -> {
            this.generatePeople(i);
        });

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade","1")
        )
                .andExpect(jsonPath("_embedded.peopleDtoList[0].grade").value(1))
                .andDo(print())
;
    }
    @Test
    @DisplayName("팀을 구하는 사람들_역할")
    @Transactional
    void getJobSeekers_role() throws Exception {
        IntStream.range(10,14).forEach(i -> {
            this.generatePeople(i);
        });

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
        IntStream.range(15,19).forEach(i -> {
            this.generatePeople(i);
        });

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("area","Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleDtoList[0].area").value("Seoul"))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_레벨_역할")
    @Transactional
    void getJobSeekers_gradeAndRole() throws Exception {
        IntStream.range(20,24).forEach(i -> {
            this.generatePeople(i);
        });

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade","2")
                .param("role","DEVELOPER")
        )
                .andExpect(jsonPath("_embedded.peopleDtoList[0].grade").value(2))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_레벨_지역")
    @Transactional
    void getJobSeekers_gradeAndArea() throws Exception {
        IntStream.range(25,29).forEach(i -> {
            this.generatePeople(i);
        });

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("grade","2")
                .param("area","Busan")
        )
                .andExpect(jsonPath("_embedded.peopleDtoList[0].grade").value(2))
                .andExpect(jsonPath("_embedded.peopleDtoList[0].area").value("Busan"))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_역할_지역")
    @Transactional
    void getJobSeekers_RoleAndArea() throws Exception {
        IntStream.range(30,34).forEach(i -> {
            this.generatePeople(i);
        });

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "10")
                .param("role","DEVELOPER")
                .param("area","Daegu")
        )
                .andExpect(jsonPath("_embedded.peopleDtoList[0].area").value("Daegu"))
                .andDo(print());
    }
    @Test
    @DisplayName("팀을 구하는 사람들_레벨_역할_지역")
    @Transactional
    void getJobSeekers_LevelAndRoleAndArea() throws Exception {
        IntStream.range(0,4).forEach(i -> {
            this.generatePeople(i);
        });

        // When & Then
        this.mockMvc.perform(get("/people")
                .param("page", "0")
                .param("size", "3")
                .param("grade","1")
                .param("role","LEADER")
                .param("area","Seoul")
        )
                .andExpect(jsonPath("_embedded.peopleDtoList[0].grade").value(1))
                .andExpect(jsonPath("_embedded.peopleDtoList[0].area").value("Seoul"))
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

                                fieldWithPath("_embedded.peopleDtoList[].userId").description("유저의 아이디"),
                                fieldWithPath("_embedded.peopleDtoList[].userName").description("유저의 이름"),
                                fieldWithPath("_embedded.peopleDtoList[].stacks").description("유저의 기술 스택"),
                                fieldWithPath("_embedded.peopleDtoList[].area").description("유저의 활동지역"),
                                fieldWithPath("_embedded.peopleDtoList[].grade").description("유저의 레벨"),
                                fieldWithPath("_embedded.peopleDtoList[].role").description("유저가 관심 있는 분야"),
                                fieldWithPath("_links.self.href").description("Self 링크"),
                                fieldWithPath("_links.first.href").description("리스트 첫번째 페이지"),
                                fieldWithPath("_links.next.href").description("다음 리스트"),
                                fieldWithPath("_links.last.href").description("이전 리스트"),
                                fieldWithPath("page.size").description("페이지당 데이터 수"),
                                fieldWithPath("page.totalElements").description("총 데이터 수"),
                                fieldWithPath("page.totalPages").description("총 페이지 수"),
                                fieldWithPath("page.number").description("현재 페이지")

                        )
                ))
        ;
    }

    public void generatePeople(int index){
        List<UsersStack> stacks1 = new ArrayList<UsersStack>();
        stacks1.add(new UsersStack(TechnicalStack.SPRINGBOOT));
        List<UsersStack> stacks2 = new ArrayList<UsersStack>();
        stacks2.add(new UsersStack(TechnicalStack.DJANGO));
        List<UsersStack> stacks3 = new ArrayList<UsersStack>();
        stacks3.add(new UsersStack(TechnicalStack.SPRINGBOOT));
        User user1 =  User.builder()
                .userId("testUser"+(3*index+1))
                .password("testpassword")
                .grade((long)1)
                .stacks(stacks1)
                .area("Seoul")
                .userName("User"+(3*index+1))
                .role(ProjectRole.LEADER)
                .build();
        User user2 = User.builder()
                .userId("testUser"+(3*index+2))
                .password("testpassword")
                .grade((long)2)
                .stacks(stacks2)
                .area("Busan")
                .userName("User"+(3*index+2))
                .role(ProjectRole.DEVELOPER)

                .build();
        User user3 =  User.builder()
                .userId("testUser"+(3*index+3))
                .password("testpassword")
                .grade((long)3)
                .stacks(stacks3)
                .area("Daegu")
                .userName("User"+(3*index+3))
                .role(ProjectRole.DEVELOPER)
                .build();
        this.userRepository.save(user1);
        this.userRepository.save(user2);
        this.userRepository.save(user3);
    }
}
