package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.dto.UpdateDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
import com.eskiiimo.web.projects.exception.ProjectNotFoundException;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.exception.RecruitNotFoundException;
import com.eskiiimo.web.projects.exception.YouAreNotLeaderException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Transactional
    public Project storeProject(ProjectDetailDto projectDetailDto, String user_id) {
        Project project = new Project();
        projectDetailDto.toEntity(project);
        project.setLeaderId(user_id);
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
    public ProjectDetailDto updateProject(Long projectId, UpdateDto updateDto, String visitorId) {
        Project project = getProjectForLeader(projectId, visitorId);
        updateDto.toEntity(project);
        return this.projectToDto(this.projectRepository.save(project));
    }

    @Transactional
    public ProjectDetailDto getProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        return this.projectToDto(project);
    }

    @Transactional
    public List<RecruitDto> getRecruits(String visitorId, Long projectId) {
        getProjectForLeader(projectId, visitorId);
        List<Recruit> recruits = this.recruitRepository.findAllByProject_ProjectId(projectId)
                .orElseThrow(() -> new RecruitNotFoundException());

        List<RecruitDto> recruitDtos = new ArrayList<RecruitDto>();
        for (Recruit recruit : recruits)
            recruitDtos.add(this.modelMapper.map(recruit, RecruitDto.class));
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

