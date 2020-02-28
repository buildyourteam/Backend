package com.eskiiimo.api.files.projectimage;


import com.eskiiimo.api.files.FileService;
import com.eskiiimo.api.files.FileUploadDto;
import com.eskiiimo.api.files.FileUploadProperties;
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
            throw new com.eskiiimo.api.files.exception.FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }


    public FileUploadDto storeProjectImage(Long projectid, MultipartFile file){
        String fileName = fileService.storeFile(file,this.projectImageLocation,projectid.toString());

        ProjectImage projectImage = new ProjectImage(projectid,this.projectImageLocation.resolve(fileName).toString());
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
        ProjectImage projectImage =projectImageRepository.findByProjectid(projectid);
        Path filePath = Paths.get(projectImage.getFilePath());
        return fileService.loadFileAsResource(filePath);
    }


}
