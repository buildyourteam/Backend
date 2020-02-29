package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.projectapply.ProjectApplyService;
import com.eskiiimo.api.projects.projectsList.ProjectListDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class ProjectDetailService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectApplyService projectApplyService;
    private final ModelMapper modelMapper;

    @Transactional
    public Project storeProject(ProjectDetailDto projectDetailDto,String user_id) {
        Project project = modelMapper.map(projectDetailDto, Project.class);
        project.update();

        Project newProject = this.projectRepository.save(project);
        this.projectApplyService.addLeader(project,user_id);
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
        Project pr = existingProject.get();
        pr.update();
        this.modelMapper.map(projectDetailDto, pr);
        Boolean isLeader=Boolean.FALSE;
        for(ProjectMember projectMember : pr.getProjectMembers()){
            if(projectMember.getRole().equals(ProjectRole.LEADER)){
                if(projectMember.getUser().getUserId().equals(userId)) {
                    isLeader = Boolean.TRUE;
                    break;
                }
            }
        }
        if(isLeader==Boolean.FALSE)
            return null;
        this.projectRepository.save(pr);
        ProjectDetailDto projectDetailDto1 = this.modelMapper.map(pr, ProjectDetailDto.class);

        return projectDetailDto1;
    }

    @Transactional
    public ProjectDetailDto getProject(Long project_id){
        Optional<Project> optionalProject = this.projectRepository.findById(project_id);
        if (optionalProject.isEmpty()) {
            return null;
        }
        Project project = optionalProject.get();

        ProjectDetailDto projectDetailDto = modelMapper.map(project,ProjectDetailDto.class);
        List<ProjectMember> projectMemberList = this.projectMemberRepository.findAllByProject_ProjectId(project_id);
        if(projectMemberList.isEmpty()){
            projectDetailDto.setMemberList(null);
            return projectDetailDto;
        }
        else{
            List<ProjectMemberResource> projectMemberListResource = new ArrayList<ProjectMemberResource>();

            for(ProjectMember projectMember: projectMemberList){
                ProjectMemberDto projectmember = ProjectMemberDto.builder()
                        .userName(projectMember.getUser().getUserName())
                        .role(projectMember.getRole())
                        .stack(projectMember.getStack())
                        .build();
                ProjectMemberResource projectMemberResource = new ProjectMemberResource(projectmember,projectMember.getUser().getUserId());
                projectMemberListResource.add(projectMemberResource);
            }
            projectDetailDto.setMemberList(projectMemberListResource);
            return projectDetailDto;
        }
    }
}
