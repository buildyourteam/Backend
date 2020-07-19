package com.eskiiimo.web.files.service;


import com.eskiiimo.web.configs.FileUploadProperties;
import com.eskiiimo.web.files.exception.CantCreateFileDirectoryException;
import com.eskiiimo.web.files.exception.FileDownloadException;
import com.eskiiimo.web.files.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.StringTokenizer;

@Service
public class FileService {
    private final Path fileLocation;


    @Autowired
    public FileService(FileUploadProperties prop) {

        this.fileLocation = Paths.get(prop.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileLocation);
        } catch (Exception e) {
            throw new CantCreateFileDirectoryException(this.fileLocation.toString(), e);
        }
    }

    public String storeFile(MultipartFile file, Path location, String id) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        StringTokenizer tockens = new StringTokenizer(fileName);
        tockens.nextToken(".");
        fileName = id + "." + tockens.nextToken();
        try {
            // 파일명에 부적합 문자가 있는지 확인한다.
            if (fileName.contains(".."))
                throw new com.eskiiimo.web.files.exception.FileNameException(fileName);

            Path targetLocation = location.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            throw new FileUploadException(fileName, e);
        }
    }

    public Resource loadFileAsResource(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists())
                return resource;
            else
                throw new FileDownloadException(filePath.getFileName().toString());
        } catch (MalformedURLException e) {
            throw new FileDownloadException(filePath.getFileName().toString(), e);
        }
    }


}
