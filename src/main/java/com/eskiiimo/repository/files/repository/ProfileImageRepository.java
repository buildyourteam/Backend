package com.eskiiimo.repository.files.repository;


import com.eskiiimo.repository.files.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage,Integer> {
    Optional<ProfileImage> findByPersonId(String personId);
}
