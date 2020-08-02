package com.eskiiimo.web.files.controller;


import com.eskiiimo.web.files.response.FileUploadResponse;
import com.eskiiimo.web.files.service.ProjectImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/projects/image/{projectId}")
public class ProjectImageController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectImageController.class);

    private final ProjectImageService projectImageService;

    /*
    Upload Image
     */

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public FileUploadResponse uploadProjectImage(
            @PathVariable Long projectId,
            @RequestParam("image") MultipartFile file
    ) {
        return this.projectImageService.storeProjectImage(projectId, file);
    }

    /*
    Download Image
     */

    @GetMapping
    public ResponseEntity<Resource> downloadProjectImage(
            @PathVariable Long projectId,
            HttpServletRequest request) {
        // Load file as Resource
        Resource resource = projectImageService.getProjectImage(projectId);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
