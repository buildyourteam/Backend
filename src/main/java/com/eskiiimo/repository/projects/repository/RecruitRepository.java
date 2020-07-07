package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.model.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    List<Recruit> findAllByPerson_PersonId(String personId);
    Optional<Recruit> findProjectRecruitByPerson_PersonIdAndProject_ProjectId(String personId, long projectId);
    List<Recruit> findAllByProject_ProjectId(Long projectId);
}
