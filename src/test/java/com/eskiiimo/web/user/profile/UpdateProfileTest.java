package com.eskiiimo.web.user.profile;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로필 수정")
public class UpdateProfileTest extends BaseControllerTest {

    @Test
    @DisplayName("프로필 수정")
    @WithMockUser(username = "user1")
    void updateProfileSuccess() throws Exception {
        testUserFactory.generateUser(1);
        UpdateProfileRequest updateProfileRequest = testProjectFactory.generateUpdateProfileRequest();

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateProfileRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-profile",
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("프로필 수정_권한 없는 사용자")
    @WithMockUser(username = "user2")
    void updateProfileFailBecause_noPermittedUser() throws Exception {
        testUserFactory.generateUser(1);
        UpdateProfileRequest updateProfileRequest = testProjectFactory.generateUpdateProfileRequest();

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateProfileRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(201))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로필 수정_로그인하지 않은 사용자")
    void updateProfileFailBecause_notLoginUser() throws Exception {
        testUserFactory.generateUser(1);
        UpdateProfileRequest updateProfileRequest = testProjectFactory.generateUpdateProfileRequest();

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(updateProfileRequest)))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }
}
