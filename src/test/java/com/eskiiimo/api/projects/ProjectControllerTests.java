package com.eskiiimo.api.projects;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.common.TestDescription;
import com.eskiiimo.api.projects.projectsList.ProjectDto;
import com.eskiiimo.api.projects.projectsList.ProjectMemberSet;
import com.eskiiimo.api.projects.projectsList.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class ProjectControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ObjectMapper objectMapper;



    @Test
    @TestDescription("정상적으로 프로젝트를 생성하는 테스트")
    public void createProject() throws Exception {
        ProjectDto project = ProjectDto.builder()
                .projectName("project1")
                .teamName("Team1")
                .endDate(LocalDateTime.of(2020,06,30,11,11))
                .description("Hi this is project1.")
                .projectField(ProjectField.SYSTEM)
                .build();
        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @TestDescription("검색기능사용 없이 전체리스트 조회")
    public void queryEventsTotal() throws Exception {
        // Given
        IntStream.range(0,30).forEach(i -> {
            this.generateEvent(i);
        });

        // When & Then
        this.mockMvc.perform(get("/api/projects")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
                .andDo(document("total-project-lists"))
        ;

    }


    @Test
    @TestDescription("직군별 and 분야별 프로젝트 리스트 조회하기")
    public void queryEventsOccupationAndField() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/api/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("occupation","developer")
                .param("field", ProjectField.WEB.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
                .andDo(document("occupation-project-list"))
        ;

    }

    @Test
    @TestDescription("only 직군별 프로젝트 리스트 조회하기")
    public void queryEventsOccupation() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/api/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("occupation","developer")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
                .andDo(document("occupation-project-list"))
        ;

    }

    @Test
    @TestDescription("only 분야별로만 프로젝트 리스트 조회하기")
    public void queryEventsField() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/api/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("field", ProjectField.WEB.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
                .andDo(document("occupation-project-list"))
        ;

    }


    private void generateEvent(int index) {

        ProjectMemberSet need_zero = new ProjectMemberSet(0,2,3,4);
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet current = new ProjectMemberSet(2,1,1,2);

        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .current(current)
                .needMembers(need_yes)
                .status(ProjectStatus.RECRUTING)
                .projectField(ProjectField.APP)
                .build();

        Project project1 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need zero 입니다.")
                .current(current)
                .needMembers(need_zero)
                .status(ProjectStatus.RECRUTING)
                .projectField(ProjectField.WEB)
                .build();

        Project project2 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .current(current)
                .needMembers(need_yes)
                .status(ProjectStatus.RECRUTING)
                .projectField(ProjectField.WEB)
                .build();

        this.projectRepository.save(project);
        this.projectRepository.save(project1);
        this.projectRepository.save(project2);

    }
    private void generateEventDeadline(int index) {

        ProjectMemberSet need_zero = new ProjectMemberSet(0,2,3,4);
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet current = new ProjectMemberSet(2,1,1,2);

        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .current(current)
                .needMembers(need_yes)
                .status(ProjectStatus.RECRUTING)
                .build();

        Project project1 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2022,03,30,23,59))
                .description("need zero 입니다.")
                .current(current)
                .needMembers(need_zero)
                .status(ProjectStatus.RECRUTING)
                .build();

        Project project2 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,03,30,23,59))
                .description("need zero 입니다.")
                .current(current)
                .needMembers(need_zero)
                .status(ProjectStatus.RECRUTING)
                .build();

        this.projectRepository.save(project);
        this.projectRepository.save(project1);
        this.projectRepository.save(project2);

    }

}
