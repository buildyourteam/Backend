package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.repository.projects.dto.UpdateDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.common.exception.ProjectNotFoundException;
import com.eskiiimo.web.common.exception.UserNotFoundException;
import com.eskiiimo.web.projects.exception.YouAreNotReaderException;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
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
    private final UserRepository userRepository;
    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Project storeProject(ProjectDetailDto projectDetailDto, String user_id) {
        Project project = new Project();
        projectDetailDto.toEntity(project);
        project.setLeaderId(user_id);
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
    public ProjectDetailDto updateProject(Long project_id, UpdateDto updateDto, String userId) {
        Project project = projectRepository.findById(project_id)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        if(!this.isLeader(project,userId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
        updateDto.toEntity(project);
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

    @Transactional
    public List<RecruitDto> getRecruits(String visitorId, Long project_id) {
        Project project = this.projectRepository.findById(project_id)
                .orElseThrow(() -> new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        this.userRepository.findByUserId(visitorId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        if (!visitorId.equals(project.getLeaderId())){
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
        }
        List<RecruitDto> recruitDtos = new ArrayList<RecruitDto>();
        List<Recruit> recruits = this.recruitRepository.findAllByProject_ProjectId(project_id);
        for(Recruit recruit : recruits){
            RecruitDto recruitDto = this.modelMapper.map(recruit, RecruitDto.class);
            recruitDtos.add(recruitDto);
        }
        return recruitDtos;
    }

    public ProjectDetailDto projectToDto(Project project){
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

