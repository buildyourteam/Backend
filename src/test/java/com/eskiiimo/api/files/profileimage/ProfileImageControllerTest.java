package com.eskiiimo.api.files.profileimage;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme= "https",uriHost = "api.eskiiimo.com" ,uriPort = 443)
@Import(RestDocsConfiguration.class)
class ProfileImageControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ProfileImageRepository profileImageRepository;
    @Test
    @WithMockUser(username="testuser")
    void uploadProfileImage() throws Exception {
        File targetFile = new File("./src/test/java/com/eskiiimo/api/files/testfiles/testimg.jpg");
        MockMultipartFile image = new MockMultipartFile(
                "image", targetFile.getName(), "image/jpeg", new FileInputStream(targetFile));

        this.mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/profile/image/{user_id}","testuser").file(image)
                .accept(MediaTypes.HAL_JSON)
                 )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("fileName").value("testuser.jpg"))
                .andExpect(jsonPath("fileDownloadUri").value("https://api.eskiiimo.com/profile/image/testuser"))
                .andExpect(jsonPath("fileType").value("image/jpeg"))
                .andExpect(jsonPath("size").value(585219))
                .andDo(print())
                .andDo(document("upload-profile-image",
                        pathParameters(
                                parameterWithName("user_id").description("user id")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("fileName").description("사진 이름"),
                                fieldWithPath("fileDownloadUri").description("사진의 Url"),
                                fieldWithPath("fileType").description("사진의 형태"),
                                fieldWithPath("size").description("사진 크기")
                        )
                ))
        ;
    }



    @Test
    void downloadProfileImage() throws Exception {
        File targetFile = new File("./src/test/java/com/eskiiimo/api/files/testfiles/testimg.jpg");

        ProfileImage profileImage = ProfileImage.builder()
                .userId("testuser")
                .filePath(targetFile.getPath())
                .build();
        profileImageRepository.save(profileImage);

        this.mockMvc.perform(get("/profile/image/testuser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
        ;
    }
}