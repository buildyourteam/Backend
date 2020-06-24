package com.eskiiimo.web.user.controller;


import com.eskiiimo.web.files.FileUploadDto;
import com.eskiiimo.web.user.service.ProfileImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/profile/image/{user_id}")
public class ProfileImageController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileImageController.class);

    @Autowired
    private ProfileImageService profileImageService;

    /*
    Upload Image
     */

    @PostMapping
    public ResponseEntity uploadProfileImage(@PathVariable String user_id, @RequestParam("image") MultipartFile file) {

        FileUploadDto fileUploadDto =  profileImageService.storeProfileImage(user_id,file);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String userId = authentication.getName();
        if(!userId.equals(user_id))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile/image/")
                .path(user_id)
                .toUriString();

        fileUploadDto.setFileDownloadUri(fileDownloadUri);

        return ResponseEntity.ok().body(fileUploadDto);
    }


    @GetMapping
    public ResponseEntity<Resource> downloadProfileImage(@PathVariable String user_id, HttpServletRequest request){
        // Load file as Resource
        Resource resource = profileImageService.getProfileImage(user_id);
        if(resource==null)
            return ResponseEntity.notFound().build();
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
