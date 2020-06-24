package com.eskiiimo.web.user.service;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.error.exception.ProjectMemberNotFoundException;
import com.eskiiimo.web.error.exception.ProjectNotFoundException;
import com.eskiiimo.web.error.exception.UserNotFoundException;
import com.eskiiimo.web.projects.enumtype.Status;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ModelMapper modelMapper;

    @Transactional
    public ProfileDto getProfile(String user_id) {
        User profile =  userRepository.findByUserId(user_id)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        ProfileDto profileDto = profile.toProfileDto();
        return profileDto;
    }

    @Transactional
    public ProfileDto updateProfile(String user_id, ProfileDto updateData) {
        User profile =  userRepository.findByUserId(user_id)
                .orElseThrow(()-> new UserNotFoundException("존재하지 않는 사용자입니다."));
        updateData.updateProfile(profile);
        return  this.userRepository.save(profile).toProfileDto();
    }

    public Page<Project> getRunning(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndStatus(user_id,Boolean.FALSE, Status.RUNNING, pageable);
        return page;
    }

    public Page<Project> getEnded(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndStatus(user_id, Boolean.FALSE,Status.ENDED, pageable);
        return page;
    }

    public Page<Project> getPlanner(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(user_id, Boolean.FALSE,pageable);
        return page;
    }
    public Page<Project> getHiddenRunning(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndStatus(user_id,Boolean.TRUE, Status.RUNNING, pageable);
        return page;
    }

    public Page<Project> getHiddenEnded(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndStatus(user_id, Boolean.TRUE,Status.ENDED, pageable);
        return page;
    }

    public Page<Project> getHiddenPlanner(String user_id, Pageable pageable) {
        Page<Project> page = this.projectRepository.findAllByLeaderIdAndProjectMembers_Hide(user_id, Boolean.TRUE,pageable);
        return page;
    }
    public void reShowProject(String user_id, Long projectId) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        ProjectMember projectMember = this.projectMemberRepository.findByProject_ProjectIdAndUser_UserId(projectId,user_id)
                .orElseThrow(()->new ProjectMemberNotFoundException("프로젝트에 소속되어있지 않습니다."));
        project.getProjectMembers().remove(projectMember);
        projectMember.setHide(Boolean.FALSE);
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }

    public void hideProject(String user_id, Long projectId) {
        Project project = this.projectRepository.findById(projectId)
                .orElseThrow(()->new ProjectNotFoundException("존재하지 않는 프로젝트입니다."));
        ProjectMember projectMember = this.projectMemberRepository.findByProject_ProjectIdAndUser_UserId(projectId,user_id)
                .orElseThrow(()->new ProjectMemberNotFoundException("프로젝트에 소속되어있지 않습니다."));
        project.getProjectMembers().remove(projectMember);
        projectMember.setHide(Boolean.TRUE);
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }
}
