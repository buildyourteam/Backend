package com.eskiiimo.api.user.projectrecruit;

import com.eskiiimo.api.error.exception.*;
import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectMember;
import com.eskiiimo.api.projects.ProjectRepository;
import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectRecruitService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectRecruitRepository projectRecruitRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void recruitProject(String userId, ProjectRecruitDto recruit, Long projectId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        User user =  userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!this.isLeader(project,visitorId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
        ProjectRecruit projectRecruit = recruit.toEntity(user, project);
        this.projectRecruitRepository.save(projectRecruit);
    }

    @Transactional
    public List<ProjectRecruitDto> getRecruitList(String userId, String visitorId) {
        this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!userId.equals(visitorId)){
            throw new RecruitNotAuthException("영입제안 권한이 없습니다.");
        }
        List<ProjectRecruitDto> projectRecruits = new ArrayList<ProjectRecruitDto>();
        List<ProjectRecruit> RecruitList=this.projectRecruitRepository.findAllByUser_UserId(visitorId);
        for(ProjectRecruit projectRecruit : RecruitList){
            ProjectRecruitDto dto = this.modelMapper.map(projectRecruit, ProjectRecruitDto.class);
            projectRecruits.add(dto);
        }
        return projectRecruits;
    }

    @Transactional
    public ProjectRecruitDto getRecruit(String userId, Long projectId, String visitorId) {
        UserAndProjectNotFoundException(userId, projectId, visitorId);
        ProjectRecruit recruit = this.projectRecruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        if(recruit.getStatus().equals(ProjectRecruitStatus.UNREAD)){
            recruit.setStatus(ProjectRecruitStatus.READ);
            this.projectRecruitRepository.save(recruit);
        }
        return this.modelMapper.map(recruit, ProjectRecruitDto.class);
    }

    @Transactional
    public void acceptRecruit(String userId, Long projectId,String visitorId) {
        UserAndProjectNotFoundException(userId, projectId, visitorId);
        ProjectRecruit recruit = this.projectRecruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        if(!recruit.getStatus().equals(ProjectRecruitStatus.ACCEPT)){
            recruit.setStatus(ProjectRecruitStatus.ACCEPT);
            this.projectRecruitRepository.save(recruit);
        }
    }

    @Transactional
    public void rejectRecruit(String userId, Long projectId, String visitorId) {
        UserAndProjectNotFoundException(userId, projectId, visitorId);
        ProjectRecruit recruit = this.projectRecruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        this.projectRecruitRepository.delete(recruit);
    }

    private void UserAndProjectNotFoundException(String userId, Long projectId, String visitorId) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        User user = this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!userId.equals(visitorId)){
            throw new RecruitNotAuthException("확인 권한이 없습니다.");
        }
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
