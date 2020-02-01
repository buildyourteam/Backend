package com.eskiiimo.api.files.projectimage;


import com.eskiiimo.api.files.FileUploadDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;

public class ProjectImageController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectImageController.class);

    @Autowired
    private ProjectImageService projectImageService;

    /*
    Upload Image
     */

    @PostMapping("/projects/image/{projectid}")
    public ResponseEntity uploadProjectImage(@PathVariable Long projectid, @RequestParam("image") MultipartFile file) {
        String fileName = projectImageService.storeProjectImage(projectid,file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/projects/image/")
                .path(projectid.toString())
                .toUriString();
        FileUploadDto fileUploadDto = new FileUploadDto(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        System.out.println(fileUploadDto.getFileDownloadUri());
        return ResponseEntity.created(Paths.get(fileDownloadUri).toUri()).body(fileUploadDto);
    }




    /*
    Download Image
     */


    @GetMapping("/projects/image/{projectid}")
    public ResponseEntity<Resource> downloadProjectImage(@PathVariable Long projectid, HttpServletRequest request){
        // Load file as Resource
        Resource resource = projectImageService.getProjectImage(projectid);

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
