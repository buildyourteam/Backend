package com.eskiiimo.repository.projects.repository;

import com.eskiiimo.repository.projects.model.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    Optional<List<Recruit>> findAllByUser_UserId(String userId);
    Optional<Recruit> findProjectRecruitByUser_UserIdAndProject_ProjectId(String userId, long projectId);
    Optional<List<Recruit>> findAllByProject_ProjectId(Long projectId);
}
