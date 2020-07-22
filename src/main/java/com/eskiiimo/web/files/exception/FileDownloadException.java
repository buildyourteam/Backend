package com.eskiiimo.web.files.exception;

public class FileDownloadException extends RuntimeException {
    public FileDownloadException(String filePath) {
        super(filePath + " 파일을 찾을 수 없습니다.");
    }

    public FileDownloadException(String filePath, Throwable cause) {
        super(filePath + " 파일을 찾을 수 없습니다.", cause);
    }
}
