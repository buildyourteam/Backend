package com.eskiiimo.web.files.service;


import com.eskiiimo.repository.files.model.ProjectImage;
import com.eskiiimo.repository.files.repository.ProjectImageRepository;
import com.eskiiimo.repository.files.dto.FileUploadDto;
import com.eskiiimo.web.configs.FileUploadProperties;
import com.eskiiimo.web.files.exception.CantCreateFileDirectoryException;
import com.eskiiimo.web.files.exception.FileDownloadException;
import com.eskiiimo.web.files.exception.ProjectImageNotFoundException;
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
        this.fileService = fileService;
        this.projectImageLocation = Paths.get(prop.getProjectimageDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.projectImageLocation);
        } catch (Exception e) {
            throw new CantCreateFileDirectoryException(this.projectImageLocation.toString(), e);
        }
    }

    public FileUploadDto storeProjectImage(Long projectId, MultipartFile file) {
        String fileName = fileService.storeFile(file, this.projectImageLocation, projectId.toString());

        ProjectImage projectImage = this.projectImageRepository.findByProjectid(projectId).orElse(new ProjectImage());
        projectImage.setProjectid(projectId);
        projectImage.setFilePath(this.projectImageLocation.resolve(fileName).toString());
        projectImageRepository.save(projectImage);
        FileUploadDto fileUploadDto = FileUploadDto.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();
        return fileUploadDto;
    }

    public Resource getProjectImage(Long projectId) {
        ProjectImage projectImage = this.projectImageRepository.findByProjectid(projectId)
                .orElseThrow(() -> new ProjectImageNotFoundException(projectId));

        Path filePath = Paths.get(projectImage.getFilePath());
        return fileService.loadFileAsResource(filePath);
    }
}
