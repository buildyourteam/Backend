package com.eskiiimo.web.files.service;


import com.eskiiimo.repository.files.model.ProfileImage;
import com.eskiiimo.repository.files.repository.ProfileImageRepository;
import com.eskiiimo.repository.files.dto.FileUploadDto;
import com.eskiiimo.web.configs.FileUploadProperties;
import com.eskiiimo.web.files.exception.FileDownloadException;
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
        this.fileService=  fileService;
        this.profileImageLocation = Paths.get(prop.getProfileimageDir())
                .toAbsolutePath().normalize();
        try {

            Files.createDirectories(this.profileImageLocation);
        }catch(Exception e) {
            throw new com.eskiiimo.web.files.exception.FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }




    public FileUploadDto storeProfileImage(String person_id, MultipartFile file){
        String fileName = fileService.storeFile(file,this.profileImageLocation,person_id);

        ProfileImage profileImage = this.profileImageRepository.findByPersonId(person_id).orElse(new ProfileImage());
                profileImage.setPersonId(person_id);
                profileImage.setFilePath(this.profileImageLocation.resolve(fileName).toString());
        profileImageRepository.save(profileImage);
        FileUploadDto fileUploadDto= FileUploadDto.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build()
        ;

        return fileUploadDto;
    }


    public Resource getProfileImage(String personId){
        ProfileImage profileImage = this.profileImageRepository.findByPersonId(personId)
                .orElseThrow(()-> new FileDownloadException("프로필 이미지가 존재하지 않습니다."));
        Path filePath = Paths.get(profileImage.getFilePath());
        return fileService.loadFileAsResource(filePath);
    }


}
