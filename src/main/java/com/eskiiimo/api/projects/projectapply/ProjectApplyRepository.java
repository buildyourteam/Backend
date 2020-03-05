package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.projects.projectapply.entity.ProjectApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectApplyRepository extends JpaRepository<ProjectApply, Long> {
   Optional<ProjectApply> findByUser_UserId(String userId);
   }
