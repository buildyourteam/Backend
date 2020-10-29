package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.dto.ProjectListDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<ProjectListDto> findAllByNeedMemberDeveloperGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);

    Page<ProjectListDto> findAllByNeedMemberDesignerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);

    Page<ProjectListDto> findAllByNeedMemberPlannerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);

    Page<ProjectListDto> findAllByNeedMemberEtcGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);

    Page<ProjectListDto> findAllByNeedMemberDeveloperGreaterThan(int i, Pageable pageable);

    Page<ProjectListDto> findAllByNeedMemberDesignerGreaterThan(int i, Pageable pageable);

    Page<ProjectListDto> findAllByNeedMemberPlannerGreaterThan(int i, Pageable pageable);

    Page<ProjectListDto> findAllByNeedMemberEtcGreaterThan(int i, Pageable pageable);

    Page<ProjectListDto> findAllByProjectField(ProjectField field, Pageable pageable);

    Page<ProjectListDto> findAllByEndDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<ProjectListDto> findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndState(String userId, Boolean hide, State state, Pageable pageable);

    Page<ProjectListDto> findAllByProjectMembers_User_UserIdAndProjectMembers_HideAndStateNot(String userId, Boolean hide, State state, Pageable pageable);

    Page<ProjectListDto> findAllByLeaderIdAndProjectMembers_HideAndProjectMembers_User_UserId(String leaderId, Boolean hide, String userId, Pageable pageable);

    Page<ProjectListDto> findAllProjectedBy(Pageable pageable);

    void deleteByProjectId(Long id);

}
