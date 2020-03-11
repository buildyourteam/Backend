package com.eskiiimo.api.projects.apply;

import com.eskiiimo.api.projects.apply.entity.ProjectApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectApplyRepository extends JpaRepository<ProjectApply, Long> {
   Optional<ProjectApply> findByUser_UserId(String userId);
   }
