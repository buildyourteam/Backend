package com.eskiiimo.api.files.profileimage;

import com.eskiiimo.api.files.projectimage.ProjectImage;
import com.eskiiimo.api.files.projectimage.ProjectImageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ProfileImageControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ProfileImageRepository profileImageRepository;
    @Test
    void uploadProfileImage() throws Exception {
        File targetFile = new File("./src/test/java/com/eskiiimo/api/files/testfiles/testimg.jpg");
        MockMultipartFile image = new MockMultipartFile(
                "image", targetFile.getName(), "image/jpeg", new FileInputStream(targetFile));

        this.mockMvc.perform(multipart("/profile/image/1").file(image))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("fileName").value(targetFile.getName()))
                .andExpect(jsonPath("fileDownloadUri").value("http://localhost:8080/profile/image/1"))
                .andExpect(jsonPath("fileType").value("image/jpeg"))
                .andExpect(jsonPath("size").value(585219))
                .andDo(print())
        ;
    }

    @Test
    void downloadProfileImage() throws Exception {
        File targetFile = new File("./src/test/java/com/eskiiimo/api/files/testfiles/testimg.jpg");

        ProfileImage profileImage = new ProfileImage((long)1,targetFile.getPath());
        profileImageRepository.save(profileImage);

        this.mockMvc.perform(get("/profile/image/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
        ;
    }
}