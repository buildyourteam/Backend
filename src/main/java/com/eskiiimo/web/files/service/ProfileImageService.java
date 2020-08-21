package com.eskiiimo.web.files.service;


import com.eskiiimo.repository.files.model.ProfileImage;
import com.eskiiimo.repository.files.repository.ProfileImageRepository;
import com.eskiiimo.web.configs.FileUploadProperties;
import com.eskiiimo.web.files.exception.CantCreateFileDirectoryException;
import com.eskiiimo.web.files.response.FileUploadResponse;
import com.eskiiimo.web.user.exception.NotYourProfileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ProfileImageService {

    private final Path profileImageLocation;

    private final FileService fileService;

    private final ProfileImageRepository profileImageRepository;

    @Autowired
    public ProfileImageService(FileUploadProperties prop, ProfileImageRepository profileImageRepository, FileService fileService) {
        this.profileImageRepository = profileImageRepository;
        this.fileService = fileService;
        this.profileImageLocation = Paths.get(prop.getProfile().getDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.profileImageLocation);
        } catch (Exception e) {
            throw new CantCreateFileDirectoryException(this.profileImageLocation.toString(), e);
        }
    }

    public FileUploadResponse storeProfileImage(String user_id, MultipartFile file) {

        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(user_id))
            throw new NotYourProfileException(user_id);

        String fileName = fileService.storeFile(file, this.profileImageLocation, user_id);

        ProfileImage profileImage = this.profileImageRepository.findByUserId(user_id).orElse(new ProfileImage());
        String filePath = this.profileImageLocation.resolve(fileName).toString();
        profileImage.updateProfileImage(user_id, filePath);
        profileImageRepository.save(profileImage);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile/image/")
                .path(user_id)
                .toUriString();

        FileUploadResponse fileUploadResponse = FileUploadResponse.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();

        return fileUploadResponse;
    }

    public Resource getProfileImage(String userId) {
        Optional<ProfileImage> image = this.profileImageRepository.findByUserId(userId);
        if (image.isEmpty())
            return fileService.loadFileAsResource("defaultImages/defaultProfileImage.png");

        return fileService.loadFileAsResource(Paths.get(image.get().getFilePath()));
    }
}
