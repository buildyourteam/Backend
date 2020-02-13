package com.eskiiimo.api.projects.projectsList;

import com.eskiiimo.api.projects.Project;
import com.eskiiimo.api.projects.ProjectField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
//    Page<Project> findAllByNeedMembersDeveloperGreaterThan(int i, Pageable pageable);
//    Page<Project> findAllByNeedMembersDeveloperGreaterThanOrProjectField(int i, String field, Pageable pageable);
    Page<Project> findAllByNeedMembersDeveloperGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMembersDesignerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMembersPlannerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMembersEtcGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMembersDeveloperGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedMembersDesignerGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedMembersPlannerGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedMembersEtcGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByProjectField(ProjectField field, Pageable pageable);
    Page<Project> findAllByDdayLessThanOrderByDdayAsc(long i, Pageable pageable);


//    Page<Project> findTop2OrderByEndDate(Pageable pageable);
    }
