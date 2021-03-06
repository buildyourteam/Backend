package com.eskiiimo.web.configs;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
    application.yml에 있는 파일경로를 가져오기 위한 클래스
 */
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileUploadProperties {
    private String uploadDir;
    private project project;
    private profile profile;

    @Getter
    @Setter
    public static class profile {
        private String dir;
        private String defaultImg;
    }

    @Getter
    @Setter
    public static class project {
        private String dir;
        private String defaultImg;
    }
}
