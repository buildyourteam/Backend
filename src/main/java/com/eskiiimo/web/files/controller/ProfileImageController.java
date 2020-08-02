package com.eskiiimo.web.files.controller;


import com.eskiiimo.web.files.response.FileUploadResponse;
import com.eskiiimo.web.files.service.ProfileImageService;
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
@RequestMapping(value = "/profile/image/{user_id}")
public class ProfileImageController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileImageController.class);

    private final ProfileImageService profileImageService;

    /*
    Upload Image
     */

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public FileUploadResponse uploadProfileImage(
            @PathVariable String user_id,
            @RequestParam("image") MultipartFile file
    ) {
        return this.profileImageService.storeProfileImage(user_id, file);
    }


    @GetMapping
    public ResponseEntity<Resource> downloadProfileImage(
            @PathVariable String user_id,
            HttpServletRequest request
    ) {
        // Load file as Resource
        Resource resource = profileImageService.getProfileImage(user_id);
        if (resource == null)
            return ResponseEntity.notFound().build();
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
