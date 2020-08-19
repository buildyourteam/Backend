package com.eskiiimo.web.common;

import com.eskiiimo.web.common.TestFactory.project.TestProjectFactory;
import com.eskiiimo.web.common.TestFactory.user.TestUserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.eskiiimo.com", uriPort = 443)
@Transactional
@Import(RestDocsConfiguration.class)
@Disabled
public class BaseControllerTest{
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TestUserFactory testUserFactory;

    @Autowired
    protected TestProjectFactory testProjectFactory;
}
