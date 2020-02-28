package com.eskiiimo.api.index;

import com.eskiiimo.api.common.RestDocsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme= "https",uriHost = "api.eskiiimo.com" ,uriPort = 443)
@Import(RestDocsConfiguration.class)
class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Test
    void mainIndex() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(jsonPath("_links.projectList.href").exists())
                .andExpect(jsonPath("_links.projectListDeadline.href").exists())
                .andExpect(jsonPath("_links.peopleList.href").exists())
                .andDo(document("index-main",
                        links(
                                linkWithRel("projectList").description("프로젝트 리스트"),
                                linkWithRel("projectListDeadline").description("마감임박 프로젝트 리스트"),
                                linkWithRel("peopleList").description("팀을 구하는 사람들 리스트 API")
                        )
                        ))
        ;
    }

    @Test
    void projectsIndex() throws Exception {
        this.mockMvc.perform(get("/index/projects"))
                .andExpect(jsonPath("_links.projectList.href").exists())
                .andDo(document("index-projects",
                        links(
                                linkWithRel("projectList").description("프로젝트 리스트")
                        )
                ))
        ;
    }

    @Test
    void projectDetailIndex() throws Exception {
        this.mockMvc.perform(get("/index/projects/1"))
                .andExpect(jsonPath("_links.projectDetail.href").exists())
                .andDo(document("index-projects-detail",
                        links(
                                linkWithRel("projectDetail").description("프로젝트 상세 데이터")
                        )
                ))
        ;
    }

    @Test
    void peopleIndex() throws Exception {
        this.mockMvc.perform(get("/index/people"))
                .andExpect(jsonPath("_links.peopleList.href").exists())
                .andDo(document("index-people",
                        links(
                                linkWithRel("peopleList").description("팀을 구하는 사람들 리스트")
                        )
                ))
        ;
    }

    @Test
    void profileIndex() throws Exception {
        this.mockMvc.perform(get("/index/profile/testUser"))
                .andExpect(jsonPath("_links.profileDetail.href").exists())
                .andDo(document("index-profile",
                        links(
                                linkWithRel("profileDetail").description("프로필"),
                                linkWithRel("endedProjectList").description("내가 완료한 프로젝트"),
                                linkWithRel("runningProjectList").description("현재 참여중인 프로젝트"),
                                linkWithRel("plannedProjectList").description("내가 기획한 프로젝트")
                        )
                ))
        ;
    }
}