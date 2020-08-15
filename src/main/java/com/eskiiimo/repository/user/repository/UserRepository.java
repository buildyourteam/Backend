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

    Optional<User> findByUserIdAndActivateIsNot(String userId, UserActivate activate);

    Optional<User> findByUserIdAndActivateAndRefreshToken(String userId, UserActivate activate, String refreshToken);

    Page<User> findAllByActivateAndState(UserActivate activate, UserState state, Pageable pageable);

    Page<User> findAllByGradeAndActivateAndState(Long grade, UserActivate activate, UserState state, Pageable pageable);

    Page<User> findAllByRoleAndActivateAndState(ProjectRole role, UserActivate activate, UserState state, Pageable pageable);

    Page<User> findAllByAreaAndActivateAndState(String area, UserActivate activate, UserState state, Pageable pageable);

    Page<User> findAllByGradeAndAreaAndActivateAndState(Long grade, String area, UserActivate activate, UserState state, Pageable pageable);

    Page<User> findAllByAreaAndRoleAndActivateAndState(String area, ProjectRole role, UserActivate activate, UserState state, Pageable pageable);

    Page<User> findAllByRoleAndGradeAndActivateAndState(ProjectRole role, Long grade, UserActivate activate, UserState state, Pageable pageable);

    Page<User> findAllByAreaAndRoleAndGradeAndActivateAndState(String area, ProjectRole role, Long grade, UserActivate activate, UserState state, Pageable pageable);

    long countAllByState(UserState state);
}

