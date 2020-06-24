package com.eskiiimo.web.files.service;


import com.eskiiimo.repository.files.model.ProjectImage;
import com.eskiiimo.repository.files.repository.ProjectImageRepository;
import com.eskiiimo.repository.files.dto.FileUploadDto;
import com.eskiiimo.repository.files.dto.FileUploadProperties;
import com.eskiiimo.web.files.exception.FileDownloadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProjectImageService {

    private final Path projectImageLocation;

    private final FileService fileService;

    private final ProjectImageRepository projectImageRepository;



    @Autowired
    public ProjectImageService(FileUploadProperties prop, ProjectImageRepository projectImageRepository, FileService fileService) {
        this.projectImageRepository = projectImageRepository;
        this.fileService=  fileService;
        this.projectImageLocation = Paths.get(prop.getProjectimageDir())
                .toAbsolutePath().normalize();
        try {

            Files.createDirectories(this.projectImageLocation);
        }catch(Exception e) {
            throw new com.eskiiimo.web.files.exception.FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }


    public FileUploadDto storeProjectImage(Long projectid, MultipartFile file){
        String fileName = fileService.storeFile(file,this.projectImageLocation,projectid.toString());

        ProjectImage projectImage = this.projectImageRepository.findByProjectid(projectid).orElse(new ProjectImage());
        projectImage.setProjectid(projectid);
        projectImage.setFilePath(this.projectImageLocation.resolve(fileName).toString());
        projectImageRepository.save(projectImage);
        FileUploadDto fileUploadDto= FileUploadDto.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build()
                ;
        return fileUploadDto;
    }


    public Resource getProjectImage(Long projectid){
        ProjectImage projectImage =this.projectImageRepository.findByProjectid(projectid)
                .orElseThrow(()->new FileDownloadException("프로필 이미지가 존재하지 않습니다."));
        Path filePath = Paths.get(projectImage.getFilePath());
        return fileService.loadFileAsResource(filePath);
    }


}
