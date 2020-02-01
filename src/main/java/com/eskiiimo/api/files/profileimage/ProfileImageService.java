package com.eskiiimo.api.files.profileimage;


import com.eskiiimo.api.files.FileService;
import com.eskiiimo.api.files.FileUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
            throw new com.restful.api.files.Exception.FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }




    public String storeProfileImage(Long memberid, MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileService.storeFile(file,this.profileImageLocation);
        ProfileImage profileImage = new ProfileImage(memberid,this.profileImageLocation.resolve(fileName).toString());
        profileImageRepository.save(profileImage);
        return fileName;
    }



    public Resource getProfileImage(Long memberid){
        ProfileImage profileImage = profileImageRepository.findByMemberid(memberid);
        Path filePath = Paths.get(profileImage.getFilePath());
        return fileService.loadFileAsResource(filePath);
    }


}
