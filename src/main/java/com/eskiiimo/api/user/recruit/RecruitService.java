package com.eskiiimo.api.user.recruit;

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
public class RecruitService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void recruitProject(String userId, RecruitDto recruit, Long projectId, String visitorId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        User user =  userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!this.isLeader(project,visitorId))
            throw new YouAreNotReaderException("당신은 팀장이 아닙니다.");
        Recruit projectRecruit = recruit.toEntity(user, project);
        this.recruitRepository.save(projectRecruit);
    }

    @Transactional
    public List<RecruitDto> getRecruitList(String userId, String visitorId) {
        this.userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));
        if(!userId.equals(visitorId)){
            throw new RecruitNotAuthException("영입제안 권한이 없습니다.");
        }
        List<RecruitDto> projectRecruits = new ArrayList<RecruitDto>();
        List<Recruit> RecruitList=this.recruitRepository.findAllByUser_UserId(visitorId);
        for(Recruit recruit : RecruitList){
            RecruitDto dto = this.modelMapper.map(recruit, RecruitDto.class);
            projectRecruits.add(dto);
        }
        return projectRecruits;
    }

    @Transactional
    public RecruitDto getRecruit(String userId, Long projectId, String visitorId) {
        UserAndProjectNotFoundCheck(userId, projectId, visitorId);
        Recruit recruit = this.recruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        if(recruit.getStatus().equals(RecruitStatus.UNREAD)){
            recruit.setStatus(RecruitStatus.READ);
            this.recruitRepository.save(recruit);
        }
        return this.modelMapper.map(recruit, RecruitDto.class);
    }

    @Transactional
    public void acceptRecruit(String userId, Long projectId,String visitorId) {
        UserAndProjectNotFoundCheck(userId, projectId, visitorId);
        Recruit recruit = this.recruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        if(!recruit.getStatus().equals(RecruitStatus.ACCEPT)){
            recruit.setStatus(RecruitStatus.ACCEPT);
            this.recruitRepository.save(recruit);
        }
    }

    @Transactional
    public void rejectRecruit(String userId, Long projectId, String visitorId) {
        UserAndProjectNotFoundCheck(userId, projectId, visitorId);
        Recruit recruit = this.recruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId).orElseThrow(()->new RecruitNotFoundException("해당 영입제안이 없습니다."));
        recruit.setStatus(RecruitStatus.REJECT);
        this.recruitRepository.save(recruit);
    }

    private void UserAndProjectNotFoundCheck(String userId, Long projectId, String visitorId) {
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