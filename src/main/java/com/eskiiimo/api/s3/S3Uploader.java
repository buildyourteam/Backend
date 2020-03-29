package com.eskiiimo.api.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 *  정적 파일을 올리는 클래스
 *  1. Multipart 파일을 전달 받는다.
 *  2. S3에 전달할 수 있도록 MultipartFile을 File로 convert
 *  3. 전환된 File을 S3에 Public & Read Only 권한으로 PUT
 *  4. 로컬에 생성된 File 삭제
 *  5. 업로드 된 파일의 S3 URL 주소를 반환
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    // S3 버킷 환경변수 설정
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 1. Multipart 파일을 전달 받는다.
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {

        // 2. Multipart -> File로 전환
        File uploadFile = convert(multipartFile).orElseThrow(
                () -> new IllegalArgumentException("MultipartFile -> File로 전환 실패!!"));

        return upload(uploadFile, dirName);
    }

    // 5. 업로드 된 파일의 S3 URL 주소를 반환
    private String upload(File uploadFile, String dirName) {

        // 로컬 PC에 저장되어 있는 파일 전체 경로 설정
        String fileName = dirName + "/" + uploadFile.getName();

        // S3에 파일 업로드
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // 3. 전환된 File을 S3에 Public & Read Only 권한으로 PUT
    private String putS3(File uploadFile, String fileName) {

        /*
            S3에 객체 upload
            파일에 대한 개별 ACL 설정

            접근 권한 : public
            사용자 권한 : read
         */
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).
                withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 4. 로컬에 생성된 File 삭제
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일 삭제에 실패했습니다.");
        }
    }

    // 2. S3에 전달할 수 있도록 MultipartFile을 File로 convert
    private Optional<File> convert(MultipartFile file) throws IOException {
        // 파라미터로 가져온 파일에 대한 이름을 기준으로 파일 객체 생성
        File convertFile = new File(file.getOriginalFilename());

        // convertFile과 같은 이름이 존재하지 않을 경우 파일 스트림에 파일 생성
        if(convertFile.createNewFile()) {
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }

            // 파일을 Optional 객체에 담아서 리턴
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

}

