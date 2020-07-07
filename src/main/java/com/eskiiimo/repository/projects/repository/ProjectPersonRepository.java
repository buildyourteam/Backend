package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.model.ProjectPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectPersonRepository extends JpaRepository<ProjectPerson,Long> {
    Optional<ProjectPerson> findByProject_ProjectIdAndPerson_PersonId(Long projectId, String personId);

}
