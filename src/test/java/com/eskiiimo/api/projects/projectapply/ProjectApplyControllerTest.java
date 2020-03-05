package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.common.RestDocsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme= "https",uriHost = "api.eskiiimo.com" ,uriPort = 443)
@Import(RestDocsConfiguration.class)
class ProjectApplyControllerTest {

    @Test
    @Transactional
    void applyProject() {
    }

    @Test
    @Transactional
    void updateApply() {
    }

    @Test
    @Transactional
    void getApplicants() {
    }

    @Test
    @Transactional
    void getApply() {
    }

    @Test
    @Transactional
    void acceptMember() {
    }

    @Test
    @Transactional
    void rejectMember() {
    }
}