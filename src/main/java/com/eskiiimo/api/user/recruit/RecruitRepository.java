package com.eskiiimo.api.user.recruit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    List<Recruit> findAllByUser_UserId(String userId);
    Optional<Recruit> findProjectRecruitByUser_UserIdAndProject_ProjectId(String userId, long projectId);
    List<Recruit> findAllByProject_ProjectId(Long projectId);
}
