package com.eskiiimo.repository.files.repository;


import com.eskiiimo.repository.files.model.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage,Integer> {
    Optional<ProjectImage> findByProjectId(Long projectId);
}
