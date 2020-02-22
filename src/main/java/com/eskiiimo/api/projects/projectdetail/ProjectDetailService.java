package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectMember;
import com.eskiiimo.api.projects.ProjectMemberRepository;
import com.eskiiimo.api.projects.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectDetailService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ModelMapper modelMapper;



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
