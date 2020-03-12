package com.eskiiimo.api.user.projectrecruit;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


public interface ProjectRecruitRepository extends JpaRepository<ProjectRecruit, Long> {
    List<ProjectRecruit> findAllByUser_UserId(String userId);
    Optional<ProjectRecruit> findProjectRecruitByUser_UserIdAndProject_ProjectId(String userId, long projectId);
}
