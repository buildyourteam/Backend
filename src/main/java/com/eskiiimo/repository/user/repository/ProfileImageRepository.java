package com.eskiiimo.repository.user.repository;


import com.eskiiimo.repository.user.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage,Integer> {
    Optional<ProfileImage> findByUserId(String userId);
}
