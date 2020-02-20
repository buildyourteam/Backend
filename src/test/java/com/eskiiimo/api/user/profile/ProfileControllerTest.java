package com.eskiiimo.api.user.profile;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.TechnicalStack;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme= "https",uriHost = "api.eskiiimo.com" ,uriPort = 443)
@Import(RestDocsConfiguration.class)
class ProfileControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getProfile() throws Exception {
        this.generateUser(2);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profiles/{userId}","user2"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("query-profile",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("updateProfile").description("프로필 업데이트"),
                                linkWithRel("profile").description("Api 명세서")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("role").description("역할군"),
                                fieldWithPath("stack").description("기술스택"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("area").description("활동지역"),
                                fieldWithPath("level").description("레벨"),
                                fieldWithPath("description").description("자기소개"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.updateProfile.href").description("프로필 업데이트"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
        ;

    }

    @Test
    void updateProfile() throws Exception {
        this.generateUser(1);
        ProfileDto profileDto = ProfileDto.builder()
                .area("서울시 구로구")
                .contact("010-9876-5432")
                .description("프로필 업데이트 하기")
                .role("LEADER")
                .stack("DJANGO")
                .userName("회원 01")
                .level((long)100)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profiles/{userId}","user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-profile",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("role").description("역할군"),
                                fieldWithPath("stack").description("기술스택"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("area").description("활동지역"),
                                fieldWithPath("level").description("레벨"),
                                fieldWithPath("description").description("자기소개")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("role").description("역할군"),
                                fieldWithPath("stack").description("기술스택"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("area").description("활동지역"),
                                fieldWithPath("level").description("레벨"),
                                fieldWithPath("description").description("자기소개"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
        ;
    }

    private User generateUser(int index){
        User user = User.builder()
                .userId("user"+index)
                .userName("회원"+index)
                .userEmail("user"+index+"@Eskiiimo.com")
                .userPassword("password")
                .area("seoul")
                .contact("010-1234-5678")
                .description("테스트용 가계정"+index)
                .level((long) (index*100))
                .role(ProjectRole.DEVELOPER)
                .stack(TechnicalStack.SPRINGBOOT)
                .build();
        this.userRepository.save(user);
        return user;

    }
}