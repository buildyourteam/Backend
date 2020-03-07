package com.eskiiimo.api.projects.projectdetail;

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
        Optional<Project> optionalProjectProject = this.projectRepository.findById(id);
        Project project = optionalProjectProject.get();
        Boolean isLeader=Boolean.FALSE;
        for(ProjectMember projectMember : project.getProjectMembers()){
            if(projectMember.getRole().equals(ProjectRole.LEADER)){
                if(projectMember.getUser().getUserId().equals(userId)) {
                    isLeader = Boolean.TRUE;
                    break;
                }
            }
        }
        if(isLeader) {
            for(ProjectMember projectMember : project.getProjectMembers()){
                this.projectMemberRepository.delete(projectMember);
            }
            this.projectRepository.deleteByProjectId(id);
            return Boolean.TRUE;
        }
        else
            return Boolean.FALSE;
    }

    @Transactional
    public ProjectDetailDto updateProject(Long project_id, ProjectDetailDto projectDetailDto,String userId) {
        Optional<Project> existingProject = this.projectRepository.findById(project_id);
        if (existingProject.isEmpty()) {
            return null;
        }
        Project project = existingProject.get();
        Boolean isLeader=Boolean.FALSE;
        for(ProjectMember projectMember : project.getProjectMembers()){
            if(projectMember.getRole().equals(ProjectRole.LEADER)){
                if(projectMember.getUser().getUserId().equals(userId)) {
                    isLeader = Boolean.TRUE;
                    break;
                }
            }
        }
        if(isLeader==Boolean.FALSE)
            return null;
        else
            projectDetailDto.toEntity(project);
        Project project1 = this.projectRepository.save(project);
        ProjectDetailDto projectDetailDto1 = this.projectToDto(project1);

        return projectDetailDto1;
    }

    @Transactional
    public ProjectDetailDto getProject(Long project_id){
        Optional<Project> optionalProject = this.projectRepository.findById(project_id);
        if (optionalProject.isEmpty())
            return null;
        Project project = optionalProject.get();
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

    }

