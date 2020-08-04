package com.eskiiimo.web.user.profile;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로필 수정")
public class UpdateProfileTest extends BaseControllerTest {

//    @Test
//    @Transactional
//    @DisplayName("프로필 수정")
//    @WithMockUser(username = "user1")
//    void updateProfile() throws Exception {
//        testUserFactory.generateUser(1);
//        UpdateProfileRequest updateProfileRequest = testProjectFactory.generateUpdateProfileRequest();
//
//        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}", "user1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.objectMapper.writeValueAsString(updateProfileRequest)))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andDo(document("update-profile",
//                        links(
//                                linkWithRel("self").description("self 링크"),
//                                linkWithRel("profile").description("Api 명세서")
//                        ),
//                        pathParameters(
//                                parameterWithName("userId").description("사용자 아이디")
//                        ),
//                        requestFields(
//                                fieldWithPath("userName").description("사용자 이름"),
//                                fieldWithPath("role").description("역할군"),
//                                fieldWithPath("stacks").description("기술스택"),
//                                fieldWithPath("contact").description("연락처"),
//                                fieldWithPath("area").description("활동지역"),
//                                fieldWithPath("introduction").description("자기소개")
//                        ),
//                        responseFields(
//                                fieldWithPath("userName").description("사용자 이름"),
//                                fieldWithPath("role").description("역할군"),
//                                fieldWithPath("stacks").description("기술스택"),
//                                fieldWithPath("contact").description("연락처"),
//                                fieldWithPath("area").description("활동지역"),
//                                fieldWithPath("grade").description("레벨"),
//                                fieldWithPath("introduction").description("자기소개"),
//                                fieldWithPath("_links.self.href").description("self 링크"),
//                                fieldWithPath("_links.profile.href").description("Api 명세서")
//                        )
//                ))
//        ;
//    }

//    @Test
//    @Transactional
//    @DisplayName("프로필 수정_권한 없는 사용자")
//    @WithMockUser(username = "user2")
//    void updateProfile_AuthX() throws Exception {
//        testUserFactory.generateUser(1);
//        UpdateProfileRequest updateProfileRequest = testProjectFactory.generateUpdateProfileRequest();
//
//        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}", "user1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.objectMapper.writeValueAsString(updateProfileRequest)))
//                .andExpect(status().isForbidden())
//                .andDo(print())
//        ;
//    }
}
