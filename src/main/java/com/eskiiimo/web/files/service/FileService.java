package com.eskiiimo.web.files.service;


import com.eskiiimo.web.configs.FileUploadProperties;
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

        }catch(Exception e) {
            throw new com.eskiiimo.web.files.exception.FileUploadException("파일을 업로드할 디렉토리를 생성하지 못했습니다.", e);
        }
    }

    public String storeFile(MultipartFile file, Path location,String id) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        StringTokenizer tockens =new StringTokenizer(fileName);
        tockens.nextToken(".");
        fileName = id+"."+ tockens.nextToken();
        try {
            // 파일명에 부적합 문자가 있는지 확인한다.
            if(fileName.contains(".."))
                throw new com.eskiiimo.web.files.exception.FileNameException("파일명에 부적합 문자가 포함되어 있습니다. " + fileName);

            Path targetLocation = location.resolve(fileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        }catch(Exception e) {
            throw new com.eskiiimo.web.files.exception.FileUploadException("["+fileName+"] 파일 업로드에 실패하였습니다. 다시 시도하십시오.",e);
        }
    }

    public Resource loadFileAsResource(Path filePath) {
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            }else {
                throw new com.eskiiimo.web.files.exception.FileDownloadException(filePath.getFileName() + " 파일을 찾을 수 없습니다.");
            }
        }catch(MalformedURLException e) {
            throw new com.eskiiimo.web.files.exception.FileDownloadException(filePath.getFileName() + " 파일을 찾을 수 없습니다.", e);
        }
    }


}
