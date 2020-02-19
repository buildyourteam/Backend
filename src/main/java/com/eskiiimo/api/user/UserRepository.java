package com.eskiiimo.api.user;

import com.eskiiimo.api.user.people.PeopleDto;
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
    Page<PeopleDto> findPeopleAll(Pageable pageable);
    @Query(nativeQuery = true)
    Page<PeopleDto> findPeopleByArea(String area,Pageable pageable);
    @Query(nativeQuery = true)
    Page<PeopleDto> findPeopleByRole(String role,Pageable pageable);
    @Query(nativeQuery = true)
    Page<PeopleDto> findPeopleByLevel(Long level,Pageable pageable);
    @Query(nativeQuery = true)
    Page<PeopleDto> findPeopleByLevelAndArea(Long level,String area,Pageable pageable);
    @Query(nativeQuery = true)
    Page<PeopleDto> findPeopleByAreaAndRole(String area,String Role,Pageable pageable);
    @Query(nativeQuery = true)
    Page<PeopleDto> findPeopleByAreaAndRoleAndLevel(String area,String role,Long level,Pageable pageable);
    @Query(nativeQuery = true)
    Page<PeopleDto> findPeopleByRoleAndLevel(String role,Long level,Pageable pageable);
    Optional<User> findByUserId(String user_id);
}

