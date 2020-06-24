package com.eskiiimo.repository.projects.repository;


import com.eskiiimo.repository.projects.model.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage,Integer> {
    Optional<ProjectImage> findByProjectid(Long projectid);
}
