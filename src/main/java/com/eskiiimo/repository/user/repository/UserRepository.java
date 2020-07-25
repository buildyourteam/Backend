package com.eskiiimo.repository.user.repository;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.user.enumtype.UserActivate;
import com.eskiiimo.web.user.enumtype.UserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdAndActivate(String userId, UserActivate activate);

    Page<User> findAllByActivate(UserActivate activate,Pageable pageable);

    Page<User> findAllByGradeAndActivate(Long grade, UserActivate activate, Pageable pageable);

    Page<User> findAllByRoleAndActivate(ProjectRole role, UserActivate activate, Pageable pageable);

    Page<User> findAllByAreaAndActivate(String area, UserActivate activate, Pageable pageable);

    Page<User> findAllByGradeAndAreaAndActivate(Long grade, String area, UserActivate activate, Pageable pageable);

    Page<User> findAllByAreaAndRoleAndActivate(String area, ProjectRole role, UserActivate activate, Pageable pageable);

    Page<User> findAllByRoleAndGradeAndActivate(ProjectRole role, Long grade, UserActivate activate, Pageable pageable);

    Page<User> findAllByAreaAndRoleAndGradeAndActivate(String area, ProjectRole role, Long grade, UserActivate activate, Pageable pageable);

    long countAllByState(UserState state);
}

