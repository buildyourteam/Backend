package com.eskiiimo.web.projects.service;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.RecruitState;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.projects.exception.ProjectNotFoundException;
import com.eskiiimo.web.projects.exception.RecruitNotAuthException;
import com.eskiiimo.web.projects.exception.RecruitNotFoundException;
import com.eskiiimo.web.projects.exception.YouAreNotLeaderException;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.exception.UserNotFoundException;
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
    public void recruitProject(String userId, RecruitDto recruit, String visitorId) {
        Project project = projectRepository.findById(recruit.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(recruit.getProjectId()));
        if (!this.isLeader(project, visitorId))
            throw new YouAreNotLeaderException(visitorId);
        User user = userRepository.findByUserIdAndActivate(userId, UserActivate.REGULAR)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Recruit projectRecruit = recruit.toEntity(user, project);
        this.recruitRepository.save(projectRecruit);
    }

    @Transactional(readOnly = true)
    public List<RecruitDto> getRecruitList(String userId, String visitorId) {
        if (!userId.equals(visitorId))
            throw new RecruitNotAuthException();

        List<Recruit> RecruitList = this.recruitRepository.findAllByUser_UserId(visitorId)
                .orElseThrow(() -> new RecruitNotFoundException());
        List<RecruitDto> projectRecruits = new ArrayList<RecruitDto>();
        for (Recruit recruit : RecruitList)
            projectRecruits.add(this.modelMapper.map(recruit, RecruitDto.class));
        return projectRecruits;
    }

    @Transactional
    public RecruitDto getRecruit(String userId, Long projectId, String visitorId) {
        Recruit recruit = getRecruitToMe(userId, projectId, visitorId);
        if (recruit.getState().equals(RecruitState.UNREAD)) {
            recruit.setState(RecruitState.READ);
            this.recruitRepository.save(recruit);
        }
        return this.modelMapper.map(recruit, RecruitDto.class);
    }

    @Transactional
    public void acceptRecruit(String userId, Long projectId, String visitorId) {
        Recruit recruit = getRecruitToMe(userId, projectId, visitorId);
        recruit.setState(RecruitState.ACCEPT);
        this.recruitRepository.save(recruit);
    }

    @Transactional
    public void rejectRecruit(String userId, Long projectId, String visitorId) {
        Recruit recruit = getRecruitToMe(userId, projectId, visitorId);
        recruit.setState(RecruitState.REJECT);
        this.recruitRepository.save(recruit);
    }

    private Recruit getRecruitToMe(String userId, Long projectId, String visitorId) {
        if (!userId.equals(visitorId))
            throw new RecruitNotAuthException();
        return this.recruitRepository.findProjectRecruitByUser_UserIdAndProject_ProjectId(userId, projectId)
                .orElseThrow(() -> new RecruitNotFoundException());
    }

    public Boolean isLeader(Project project, String visitorId) {
        for (ProjectMember projectMember : project.getProjectMembers())
            if (projectMember.getRole().equals(ProjectRole.LEADER))
                if (projectMember.getUser().getUserId().equals(visitorId))
                    return Boolean.TRUE;
        return Boolean.FALSE;
    }


}
