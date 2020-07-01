package com.eskiiimo.repository.user.repository;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.user.enumtype.UserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllByGrade(Long grade, Pageable pageable);
    Page<User> findAllByRole(ProjectRole role, Pageable pageable);
    Page<User> findAllByArea(String area, Pageable pageable);
    Page<User> findAllByGradeAndArea(Long grade, String area, Pageable pageable);
    Page<User> findAllByAreaAndRole(String area, ProjectRole role,Pageable pageable);
    Page<User> findAllByRoleAndGrade(ProjectRole role, Long grade, Pageable pageable);
    Page<User> findAllByAreaAndRoleAndGrade(String area, ProjectRole role, Long grade, Pageable pageable);
    long countAllByUserState(UserState userState);
}

