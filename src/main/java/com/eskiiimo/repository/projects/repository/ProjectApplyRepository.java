package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.model.ProjectApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectApplyRepository extends JpaRepository<ProjectApply, Long> {
   Optional<ProjectApply> findByPerson_PersonId(String personId);
   }
