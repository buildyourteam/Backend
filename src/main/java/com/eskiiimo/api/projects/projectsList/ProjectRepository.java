package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByCurrentDeveloper(int num);
    List<Project> findAllByCurrentDesigner(String designer);
    List<Project> findAllByCurrentEtc(String etc);
    List<Project> findAllByCurrentPlanner(String planner);
    Page<Project> findAllByCurrentDeveloperGreaterThan(int num, Pageable pageable);
    Page<Project> findAllByNeedMembersDeveloperGreaterThan(int i, Pageable pageable);
    Page<Project> findAllByNeedMembersDesignerGreaterThan(int i, Pageable pageable);

    Page<Project> findAllByNeedMembersPlannerGreaterThan(int i, Pageable pageable);

    Page<Project> findAllByNeedMembersEtcGreaterThan(int i, Pageable pageable);
}
