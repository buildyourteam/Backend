package com.eskiiimo.api.files.projectimage;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectImageRepository extends JpaRepository<ProjectImage,Integer> {
    ProjectImage findByProjectid(Long projectid);
}
