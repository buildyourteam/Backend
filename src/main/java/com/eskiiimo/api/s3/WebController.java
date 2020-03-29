package com.eskiiimo.api.s3;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// S3 related ref : https://jojoldu.tistory.com/300

@RequiredArgsConstructor
@RestController
public class WebController {

    private final S3Uploader s3Uploader;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     *
     * @param multipartFile     // Front에서 로컬 PC에 저장된 파일 Path 가져오기
     *                          ex) C:\desktop\image.img
     * @return                  // AWS S3에 업로드 후 해당 URL 반환
     *                          ex) egluu/1/image.img
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

}
