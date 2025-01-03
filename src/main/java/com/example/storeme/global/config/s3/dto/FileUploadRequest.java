package com.example.storeme.global.config.s3.dto;

import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.config.s3.constant.FileContentType;
import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.exception.ImageFileException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
@Builder
public class FileUploadRequest {

    private final S3Folder s3Folder;

    private final List<MultipartFile> fileList;

    public static FileUploadRequest of(S3Folder s3Folder, List<MultipartFile> fileList) {
        validate(fileList);
        return FileUploadRequest.builder()
                .s3Folder(s3Folder)
                .fileList(fileList)
                .build();
    }

    public static FileUploadRequest of(S3Folder s3Folder, MultipartFile file) {
        List<MultipartFile> fileList = Collections.singletonList(file);
        return of(s3Folder, fileList);
    }

    // 파일 유효성 검사
    private static void validate(List<MultipartFile> fileList) {
        for(MultipartFile file : fileList) {
            if (!StringUtils.hasText(file.getOriginalFilename())) {
                log.debug("filename is empty");
                throw new ImageFileException(ErrorStatus._BAD_REQUEST);
            }
            if (!isAllowedContentType(file.getContentType())) {
                log.debug("file content type is not allowed");
                throw new ImageFileException(ErrorStatus._BAD_REQUEST);
            }
        }
    }

    // 파일 확장자 검증
    private static boolean isAllowedContentType(String contentType) {
        log.info("file content type for upload: {}", contentType);
        return FileContentType.contains(contentType);
    }

    public FileCreateDto toModel() {
        return FileCreateDto.builder()
                .s3Folder(s3Folder)
                .fileList(fileList)
                .build();
    }

}
