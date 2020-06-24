package com.eskiiimo.web.files.controller;

import com.eskiiimo.repository.files.model.ProjectImage;
import com.eskiiimo.repository.files.repository.ProjectImageRepository;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

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

@DisplayName("프로젝트 이미지")
class ProjectImageControllerTest extends BaseControllerTest {

    @Autowired
    protected ProjectImageRepository projectImageRepository;

    @Test
    @Transactional
    @DisplayName("프로젝트 이미지 업로드")
    @WithMockUser(username="testuser")
    void uploadProjectImage() throws Exception {
        File targetFile = new File("./src/test/java/com/eskiiimo/web/files/testfiles/testimg.jpg");
        MockMultipartFile image = new MockMultipartFile(
                "image", targetFile.getName(), "image/jpeg", new FileInputStream(targetFile));

       this.mockMvc.perform(RestDocumentationRequestBuilders.fileUpload("/projects/image/{projectid}",1).file(image)                .accept(MediaTypes.HAL_JSON)
       )
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(jsonPath("fileName").value("1.jpg"))
               .andExpect(jsonPath("fileDownloadUri").value("https://api.eskiiimo.com/projects/image/1"))
               .andExpect(jsonPath("fileType").value("image/jpeg"))
               .andExpect(jsonPath("size").value(585219))
               .andDo(print())
               .andDo(document("upload-project-image",
                       pathParameters(
                         parameterWithName("projectid").description("Project Id")
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
    @DisplayName("프로젝트 이미지 다운로드")
    @Transactional
    void downloadProjectImage () throws Exception {
        File targetFile = new File("./src/test/java/com/eskiiimo/web/files/testfiles/testimg.jpg");

        ProjectImage projectImage = new ProjectImage((long)1,targetFile.getPath());
        projectImageRepository.save(projectImage);

        this.mockMvc.perform(get("/projects/image/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
        ;

    }
}