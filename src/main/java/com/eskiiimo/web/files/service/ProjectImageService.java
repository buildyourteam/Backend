package com.eskiiimo.web.files.service;


import com.eskiiimo.repository.files.model.ProfileImage;
import com.eskiiimo.repository.files.model.ProjectImage;
import com.eskiiimo.repository.files.repository.ProjectImageRepository;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.configs.FileUploadProperties;
import com.eskiiimo.web.files.exception.CantCreateFileDirectoryException;
import com.eskiiimo.web.files.response.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ProjectImageService {

    private final Path projectImageLocation;

    private final FileService fileService;

    private final ProjectImageRepository projectImageRepository;

    private String defaultProjectImage;

    @Autowired
    public ProjectImageService(FileUploadProperties prop, ProjectImageRepository projectImageRepository, FileService fileService) {
        this.projectImageRepository = projectImageRepository;
        this.fileService = fileService;
        this.projectImageLocation = Paths.get(prop.getProject().getDir())
                .toAbsolutePath().normalize();
        this.defaultProjectImage = prop.getProject().getDefaultImg();
        try {
            Files.createDirectories(this.projectImageLocation);
        } catch (Exception e) {
            throw new CantCreateFileDirectoryException(this.projectImageLocation.toString(), e);
        }
    }

    public FileUploadResponse storeProjectImage(Long projectId, MultipartFile file) {
        String fileName = fileService.storeFile(file, this.projectImageLocation, projectId.toString());

        ProjectImage projectImage = this.projectImageRepository.findByProjectId(projectId).orElse(new ProjectImage());
        String filePath = this.projectImageLocation.resolve(fileName).toString();
        projectImage.updateProjectImage(projectId, filePath);
        projectImageRepository.save(projectImage);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/projects/image/")
                .path(projectId.toString())
                .toUriString();

        FileUploadResponse fileUploadResponse = FileUploadResponse.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();
        return fileUploadResponse;
    }

    public Resource getProjectImage(Long projectId) {
        Optional<ProjectImage> image = this.projectImageRepository.findByProjectId(projectId);
        if (image.isEmpty())
            return fileService.loadFileAsResource("defaultImages/defaultProjectImage.png");

        return fileService.loadFileAsResource(Paths.get(image.get().getFilePath()));
    }
}
