package com.eskiiimo.api.files.profileimage;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Integer> {
    ProfileImage findByMemberid(Long Memerid);
}
