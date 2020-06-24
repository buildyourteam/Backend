package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
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
    Page<Project> findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndStatus(String userId, Boolean hide, Status status, Pageable pageable);
    Page<Project> findAllByLeaderIdAndProjectMembers_Hide(String leaderId, Boolean hide,Pageable pageable);
    Page<Project> findAllByProjectMembers_User_UserId(String user_id, Pageable pageable);
    long countAllByStatus(Status status);
    void deleteByProjectId(Long id);

    }
