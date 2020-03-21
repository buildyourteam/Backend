package com.eskiiimo.api.files.projectimage;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectImageRepository extends JpaRepository<ProjectImage,Integer> {
    Optional<ProjectImage> findByProjectid(Long projectid);
}
