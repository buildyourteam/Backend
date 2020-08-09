package com.eskiiimo.web.user.profile;

import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로필 조회")
public class GetProfileTest extends BaseControllerTest {
    @Test
    @DisplayName("본인의 프로필 조회")
    @WithMockUser(username = "user1")
    void getMyProfileSuccess() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.updateProfile.href").exists())
                .andDo(print())
                .andDo(document("query-my-profile",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("updateProfile").description("프로필 업데이트"),
                                linkWithRel("recruits").description("나에게 온 영입제안들"),
                                linkWithRel("profile").description("Api 명세서")
                        )))
        ;

    }


    @Test
    @DisplayName("프로필 조회")
    void getProfileSuccess() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.updateProfile.href").doesNotExist())
                .andDo(print())
                .andDo(document("query-profile",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디")
                        )
                ))
        ;

    }

}
