package com.eskiiimo.api.projects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByNeedMemberDeveloperGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMemberDesignerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMemberPlannerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMemberEtcGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedMemberDeveloperGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedMemberDesignerGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedMemberPlannerGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedMemberEtcGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByProjectField(ProjectField field, Pageable pageable);
    Page<Project> findAllByDdayLessThanOrderByDdayAsc(long i, Pageable pageable);
    Page<Project> findAllByProjectStatus_UserIdAndProjectStatus_Status(String user_id,String projectStatus, Pageable pageable);
    Page<Project> findAllByProjectStatus_UserIdAndProjectStatus_Plan(String user_id, Boolean plan, Pageable pageable);
    long countAllByStatus(Status status);
    void deleteByProjectId(Long id);

    }
