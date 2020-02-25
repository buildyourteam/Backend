package com.eskiiimo.api.user;

import com.eskiiimo.api.user.people.People;
import com.eskiiimo.api.user.profile.ProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true)
    Optional<ProfileDto> findProfileByUserId(String user_id);
    @Query(nativeQuery = true)
    Page<People> findPeopleAll(Pageable pageable);
    @Query(nativeQuery = true)
    Page<People> findPeopleByArea(String area, Pageable pageable);
    @Query(nativeQuery = true)
    Page<People> findPeopleByRole(String role, Pageable pageable);
    @Query(nativeQuery = true)
    Page<People> findPeopleByLevel(Long level, Pageable pageable);
    @Query(nativeQuery = true)
    Page<People> findPeopleByLevelAndArea(Long level, String area, Pageable pageable);
    @Query(nativeQuery = true)
    Page<People> findPeopleByAreaAndRole(String area, String Role, Pageable pageable);
    @Query(nativeQuery = true)
    Page<People> findPeopleByAreaAndRoleAndLevel(String area, String role, Long level, Pageable pageable);
    @Query(nativeQuery = true)
    Page<People> findPeopleByRoleAndLevel(String role, Long level, Pageable pageable);
    Optional<User> findByUserId(String user_id);
}

