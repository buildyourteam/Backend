package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByNeedMembersDeveloperGreaterThan(int i, Pageable pageable);
    Page<Project> findAllByNeedMembersDesignerGreaterThan(int i, Pageable pageable);
    Page<Project> findAllByNeedMembersPlannerGreaterThan(int i, Pageable pageable);
    Page<Project> findAllByNeedMembersEtcGreaterThan(int i, Pageable pageable);
//    Page<Project> findTop2OrderByEndDate(Pageable pageable);
    }
