package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
