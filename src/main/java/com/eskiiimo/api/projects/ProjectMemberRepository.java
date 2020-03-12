package com.eskiiimo.api.projects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    Optional<ProjectMember> findByProject_ProjectIdAndUser_UserId(Long projectId,String userId);

}
