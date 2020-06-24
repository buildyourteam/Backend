package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    Optional<ProjectMember> findByProject_ProjectIdAndUser_UserId(Long projectId,String userId);

}
