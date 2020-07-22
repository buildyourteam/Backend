package com.eskiiimo.web.files.exception;

public class CantCreateFileDirectoryException extends RuntimeException {
    public CantCreateFileDirectoryException(String fileLotation, Throwable cause) {
        super("FilePath: '" + fileLotation + "' 파일을 업로드할 디렉토리를 생성하지 못했습니다.", cause);
    }
}
