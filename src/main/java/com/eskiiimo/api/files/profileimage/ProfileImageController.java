package com.eskiiimo.api.files.profileimage;


import com.eskiiimo.api.files.FileUploadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(value = "/profile/image/{memberid}")
public class ProfileImageController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileImageController.class);

    @Autowired
    private ProfileImageService profileImageService;

    /*
    Upload Image
     */

    @PostMapping
    public ResponseEntity uploadProfileImage(@PathVariable Long memberid, @RequestParam("image") MultipartFile file) {

        FileUploadDto fileUploadDto =  profileImageService.storeProfileImage(memberid,file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/profile/image/")
                .path(memberid.toString())
                .toUriString();

        fileUploadDto.setFileDownloadUri(fileDownloadUri);

        return ResponseEntity.ok().body(fileUploadDto);
    }


    @GetMapping
    public ResponseEntity<Resource> downloadProfileImage(@PathVariable Long memberid, HttpServletRequest request){
        // Load file as Resource
        Resource resource = profileImageService.getProfileImage(memberid);
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
