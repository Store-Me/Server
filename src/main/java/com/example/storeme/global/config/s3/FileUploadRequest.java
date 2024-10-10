package com.example.storeme.global.config.s3;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@Getter
@Builder
public class FileUploadRequest {

    private final MultipartFile file;

    public static FileUploadRequest of(MultipartFile file) {
        validate(file);
        return FileUploadRequest.builder()
                .file(file)
                .build();
    }

    private static void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.debug("file is empty");
            throw new IllegalArgumentException();
        }
        if (!StringUtils.hasText(file.getOriginalFilename())) {
            log.debug("filename is empty");
            throw new IllegalArgumentException();
        }
        if (!isAllowedExtensions(file.getOriginalFilename())) {
            log.debug("filename extension is not allowed");
            throw new IllegalArgumentException();
        }
    }

    private static boolean isAllowedExtensions(String filename) {
        int fileExtensionStartIndex = filename.lastIndexOf(".") + 1;
        String extension = filename.substring(fileExtensionStartIndex).toLowerCase();
        
        return FileExtension.contains(extension);
    }

    public FileCreate toModel() {
        return FileCreate.builder()
                .file(file)
                .build();
    }

}
