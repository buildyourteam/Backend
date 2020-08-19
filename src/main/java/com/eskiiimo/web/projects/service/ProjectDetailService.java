package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.exception.EmptyRecruitListException;
import com.eskiiimo.web.projects.exception.ProjectNotFoundException;
import com.eskiiimo.web.projects.exception.RecruitNotFoundException;
import com.eskiiimo.web.projects.exception.YouAreNotLeaderException;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectDetailService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectApplyService projectApplyService;
    private final RecruitRepository recruitRepository;

    @Transactional
    public Project storeProject(ProjectDetailRequest projectDetailRequest, String user_id) {
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        for (String question : projectDetailRequest.getQuestions())
            questions.add(ProjectApplyQuestion.builder().question(question).build());

        Project project = Project.builder()
                .projectName(projectDetailRequest.getProjectName())
                .teamName(projectDetailRequest.getTeamName())
                .endDate(projectDetailRequest.getEndDate())
                .introduction(projectDetailRequest.getIntroduction())
                .state(projectDetailRequest.getState())
                .projectField(projectDetailRequest.getProjectField())
                .currentMember(new ProjectMemberSet(0, 0, 0, 0))
                .needMember(projectDetailRequest.getNeedMember())
                .leaderId(user_id)
                .applyCanFile(projectDetailRequest.getApplyCanFile())
                .questions(questions)
                .build();

        Project newProject = this.projectRepository.save(project);
        this.projectApplyService.addLeader(newProject, user_id);
        return newProject;
    }

    @Transactional
    public void deleteProject(Long projectId, String visitorId) {
        Project project = getProjectForLeader(projectId, visitorId);
        for (ProjectMember projectMember : project.getProjectMembers())
            this.projectMemberRepository.delete(projectMember);
        this.projectRepository.deleteByProjectId(projectId);
    }

    @Transactional
    public ProjectDetailDto updateProject(Long projectId, ProjectDetailRequest projectDetailRequest, String visitorId) {
        Project project = getProjectForLeader(projectId, visitorId);
        project.updateProject(
                projectDetailRequest.getProjectName(),
                projectDetailRequest.getTeamName(),
                projectDetailRequest.getEndDate(),
                projectDetailRequest.getIntroduction(),
                projectDetailRequest.getState(),
                projectDetailRequest.getProjectField(),
                projectDetailRequest.getNeedMember(),
                projectDetailRequest.getApplyCanFile(),
                projectDetailRequest.getQuestions()
        );
        return this.projectToDto(this.projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public ProjectDetailDto getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        return this.projectToDto(project);
    }

    @Transactional(readOnly = true)
    public List<RecruitDto> getRecruits(String visitorId, Long projectId) {
        getProjectForLeader(projectId, visitorId);
        List<Recruit> recruits = this.recruitRepository.findAllByProject_ProjectId(projectId)
                .orElseThrow(() -> new EmptyRecruitListException(visitorId));

        List<RecruitDto> recruitDtos = new ArrayList<RecruitDto>();
        for (Recruit recruit : recruits) {
            RecruitDto recruitDto = RecruitDto.builder()
                    .introduction(recruit.getIntroduction())
                    .projectId(recruit.getProject().getProjectId())
                    .projectName(recruit.getProject().getProjectName())
                    .role(recruit.getRole())
                    .state(recruit.getState())
                    .userName(recruit.getUser().getUserName())
                    .build();
            recruitDtos.add(recruitDto);
        }
        return recruitDtos;
    }

    public ProjectDetailDto projectToDto(Project project) {
        ProjectDetailDto projectDetailDto = ProjectDetailDto.builder()
                .projectName(project.getProjectName())
                .teamName(project.getTeamName())
                .endDate(project.getEndDate())
                .introduction(project.getIntroduction())
                .state(project.getState())
                .needMember(project.getNeedMember())
                .memberList(project.getProjectMembers())
                .questions(project.getQuestions())
                .applyCanFile(project.getApplyCanFile())
                .currentMember(project.getCurrentMember())
                .projectField(project.getProjectField())
                .build();

        return projectDetailDto;
    }

    private Boolean isLeader(Project project, String visitorId) {
        for (ProjectMember projectMember : project.getProjectMembers())
            if (projectMember.getRole().equals(ProjectRole.LEADER))
                if (projectMember.getUser().getUserId().equals(visitorId))
                    return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private Project getProjectForLeader(Long projectId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        if (!this.isLeader(project, visitorId))
            throw new YouAreNotLeaderException(visitorId);
        return project;
    }
}

