package com.eskiiimo.web.index.controller;

import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@DisplayName("인덱스")
class IndexControllerTest  extends BaseControllerTest {

    @Test
    @DisplayName("프로젝트 상세 페이지 인덱스")
    void projectDetailIndexSuccess() throws Exception {
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
    @DisplayName("프로필 페이지 인덱스")
    void profileIndexSuccess() throws Exception {
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