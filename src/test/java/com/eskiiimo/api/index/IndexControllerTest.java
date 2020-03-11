package com.eskiiimo.api.index;

import com.eskiiimo.api.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@DisplayName("인덱스")
class IndexControllerTest  extends BaseControllerTest {


    @Test
    @DisplayName("메인 페이지 인덱스")
    @Transactional
    void mainIndex() throws Exception {
        this.mockMvc.perform(get("/index"))
                .andExpect(jsonPath("_links.projectList.href").exists())
                .andExpect(jsonPath("_links.projectListDeadline.href").exists())
                .andExpect(jsonPath("_links.peopleList.href").exists())
                .andDo(document("index-main",
                        links(
                                linkWithRel("projectList").description("프로젝트 리스트"),
                                linkWithRel("projectListDeadline").description("마감임박 프로젝트 리스트"),
                                linkWithRel("peopleList").description("팀을 구하는 사람들 리스트 API"),
                                linkWithRel("createProject").description("프로젝트 만들기")
                        )
                        ))
        ;
    }

    @Test
    @DisplayName("프로젝트 리스트 페이지 인덱스")
    @Transactional
    void projectsIndex() throws Exception {
        this.mockMvc.perform(get("/index/projects"))
                .andExpect(jsonPath("_links.projectList.href").exists())
                .andDo(document("index-projects",
                        links(
                                linkWithRel("projectList").description("프로젝트 리스트"),
                                linkWithRel("createProject").description("프로젝트 만들기")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("프로젝트 상세 페이지 인덱스")
    @Transactional
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
    @DisplayName("피플 페이지 인덱스")
    @Transactional
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
    @DisplayName("프로필 페이지 인덱스")
    @Transactional
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