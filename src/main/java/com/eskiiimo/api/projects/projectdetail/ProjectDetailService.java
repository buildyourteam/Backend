package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.error.exception.ProjectNotFoundException;
import com.eskiiimo.api.error.exception.YouAreNotReaderException;
import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.projectapply.ProjectApplyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectDetailService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectApplyService projectApplyService;
    private final ModelMapper modelMapper;

    @Transactional
    public Project storeProject(ProjectDetailDto projectDetailDto,String user_id) {
        Project project = new Project();
        projectDetailDto.toEntity(project);
        Project newProject = this.projectRepository.save(project);
        this.projectApplyService.addLeader(newProject,user_id);
        return newProject;
    }

    @Transactional
    public Boolean deleteProject(Long id,String userId) {
        Project project = projectRepository.findById(id)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(!this.isLeader(project,userId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
            for(ProjectMember projectMember : project.getProjectMembers()){
                this.projectMemberRepository.delete(projectMember);
            }
            this.projectRepository.deleteByProjectId(id);
            return Boolean.TRUE;

    }

    @Transactional
    public ProjectDetailDto updateProject(Long project_id, ProjectDetailDto projectDetailDto,String userId) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(!this.isLeader(project,userId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
        projectDetailDto.toEntity(project);
        Project project1 = this.projectRepository.save(project);
        ProjectDetailDto projectDetailDto1 = this.projectToDto(project1);

        return projectDetailDto1;
    }

    @Transactional
    public ProjectDetailDto getProject(Long project_id){
        Project project = projectRepository.findById(project_id)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
            return this.projectToDto(project);
        }
    public ProjectDetailDto projectToDto(Project project){
        ProjectDetailDto projectDetailDto = ProjectDetailDto.builder()
                .projectName(project.getProjectName())
                .teamName(project.getTeamName())
                .endDate(project.getEndDate())
                .description(project.getDescription())
                .status(project.getStatus())
                .needMember(project.getNeedMember())
                .memberList(project.getProjectMembers())
                .questions(project.getQuestions())
                .applyCanFile(project.getApplyCanFile())
                .currentMember(project.getCurrentMember())
                .dday(project.getDday())
                .projectField(project.getProjectField())
                .build();

        return projectDetailDto;

    }
    public Boolean isLeader(Project project,String visitorId){
        for(ProjectMember projectMember : project.getProjectMembers()){
            if(projectMember.getRole().equals(ProjectRole.LEADER)){
                if(projectMember.getUser().getUserId().equals(visitorId)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }
    }

