package com.eskiiimo.api.projects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember,Long> {
    List<ProjectMember> findAllByProject_ProjectId(Long projectId);
}
