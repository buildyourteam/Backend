package com.eskiiimo.api.user.account;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme= "https",uriHost = "api.eskiiimo.com" ,uriPort = 443)
@Import(RestDocsConfiguration.class)
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void signin() throws Exception {

        SignUpDto signUpDto = SignUpDto.builder()
                .userId("user1")
                .userEmail("tester@eskiiimo.com")
                .name("testUser")
                .password("testPassword")
                .build();
        this.mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signUpDto)));
//
                SignInDto signInDto = SignInDto.builder()
                .userId("user1")
                .password("testPassword")
                .build();

        this.mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signInDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-AUTH-TOKEN"))
                .andDo(print())
                .andDo(document("signin",
                        requestFields(
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("X-AUTH-TOKEN").description("로그인 토큰")
                        )
                ))
        ;

    }

    @Test
    void Signup() throws Exception {
        SignUpDto signUpDto = SignUpDto.builder()
                .userId("testid")
                .userEmail("tester@eskiiimo.com")
                .name("testUser")
                .password("testPassword")
                .build();

        this.mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signUpDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("signup",
                        requestFields(
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("userEmail").description("이메일주소")
                        )
                        ))
        ;
    }

    @Test
    @WithMockUser(username = "tester")
    void icCheckNo() throws Exception {
        this.mockMvc.perform(post("/auth/idcheck/{checkId}","tester"))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("idCheck"))
        ;
    }

    @Test
    @WithMockUser(username = "tester")
    void icCheckOk() throws Exception {
        this.mockMvc.perform(post("/auth/idcheck/{checkId}","test"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("idCheckOk"))
        ;
    }
}