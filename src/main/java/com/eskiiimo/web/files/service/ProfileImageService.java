package com.eskiiimo.web.files.service;


import com.eskiiimo.repository.files.dto.FileUploadDto;
import com.eskiiimo.repository.files.model.ProfileImage;
import com.eskiiimo.repository.files.repository.ProfileImageRepository;
import com.eskiiimo.web.configs.FileUploadProperties;
import com.eskiiimo.web.files.exception.CantCreateFileDirectoryException;
import com.eskiiimo.web.files.exception.ProfileImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileImageService {

    private final Path profileImageLocation;

    private final FileService fileService;

    private final ProfileImageRepository profileImageRepository;

    @Autowired
    public ProfileImageService(FileUploadProperties prop, ProfileImageRepository profileImageRepository, FileService fileService) {
        this.profileImageRepository = profileImageRepository;
        this.fileService = fileService;
        this.profileImageLocation = Paths.get(prop.getProfileimageDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.profileImageLocation);
        } catch (Exception e) {
            throw new CantCreateFileDirectoryException(this.profileImageLocation.toString(), e);
        }
    }

    public FileUploadDto storeProfileImage(String user_id, MultipartFile file) {
        String fileName = fileService.storeFile(file, this.profileImageLocation, user_id);

        ProfileImage profileImage = this.profileImageRepository.findByUserId(user_id).orElse(new ProfileImage());
        profileImage.setUserId(user_id);
        profileImage.setFilePath(this.profileImageLocation.resolve(fileName).toString());
        profileImageRepository.save(profileImage);
        FileUploadDto fileUploadDto = FileUploadDto.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();

        return fileUploadDto;
    }

    public Resource getProfileImage(String userId) {
        ProfileImage profileImage = this.profileImageRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileImageNotFoundException(userId));

        Path filePath = Paths.get(profileImage.getFilePath());
        return fileService.loadFileAsResource(filePath);
    }
}
