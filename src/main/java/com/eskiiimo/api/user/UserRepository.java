package com.eskiiimo.api.user;

import com.eskiiimo.api.user.profile.ProfileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true)
    Optional<ProfileDto> findProfileByUserId(String user_id);

    Optional<User> findByUserId(String user_id);
}

