package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findAllByNeedPersonDeveloperGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedPersonDesignerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedPersonPlannerGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedPersonEtcGreaterThanAndProjectField(int i, ProjectField field, Pageable pageable);
    Page<Project> findAllByNeedPersonDeveloperGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedPersonDesignerGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedPersonPlannerGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByNeedPersonEtcGreaterThan(int i,  Pageable pageable);
    Page<Project> findAllByProjectField(ProjectField field, Pageable pageable);
    Page<Project> findAllByDdayLessThanOrderByDdayAsc(long i, Pageable pageable);
    Page<Project> findAllByProjectPersons_Person_PersonIdAndProjectPersons_HideAndRecruitStatus(String personId, Boolean hide, RecruitStatus recruitStatus, Pageable pageable);
    Page<Project> findAllByLeaderIdAndProjectPersons_Hide(String leaderId, Boolean hide,Pageable pageable);
    Page<Project> findAllByProjectPersons_Person_PersonId(String person_id, Pageable pageable);
    long countAllByRecruitStatus(RecruitStatus recruitStatus);
    void deleteByProjectId(Long id);

    }
