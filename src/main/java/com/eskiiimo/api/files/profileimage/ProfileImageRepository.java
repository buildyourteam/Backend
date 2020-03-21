package com.eskiiimo.api.files.profileimage;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Integer> {
    Optional<ProfileImage> findByUserId(String userId);
}
