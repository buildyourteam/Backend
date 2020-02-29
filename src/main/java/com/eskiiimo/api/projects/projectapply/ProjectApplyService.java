package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectApplyService {

    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void addLeader(Project project, String userId){
        Optional<User> optionalUser = this.userRepository.findByUserId(userId);
        User user = optionalUser.get();
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.LEADER)
                .user(user)
                .project(project)
                .build();
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }
}
