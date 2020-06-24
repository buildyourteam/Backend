package com.eskiiimo.repository.user.repository;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.user.enumtype.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllByLevel(Long level, Pageable pageable);
    Page<User> findAllByRole(ProjectRole role, Pageable pageable);
    Page<User> findAllByArea(String area, Pageable pageable);
    Page<User> findAllByLevelAndArea(Long level, String area, Pageable pageable);
    Page<User> findAllByAreaAndRole(String area, ProjectRole role,Pageable pageable);
    Page<User> findAllByRoleAndLevel(ProjectRole role, Long level, Pageable pageable);
    Page<User> findAllByAreaAndRoleAndLevel(String area, ProjectRole role, Long level, Pageable pageable);
    long countAllByStatus(UserStatus status);
}

