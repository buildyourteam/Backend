package com.eskiiimo.api.files;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FileUploadDto {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;
}
