package com.example.storeme.global.config.s3.service;

import com.example.storeme.global.common.code.status.ErrorStatus;
import com.example.storeme.global.config.s3.constant.AmazonS3BucketProperties;
import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.dto.FileCreateDto;
import com.example.storeme.global.config.s3.dto.FileUploadRequest;
import com.example.storeme.global.config.s3.exception.ImageFileException;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3BucketService{
    private final AmazonS3BucketProperties s3BucketProperties;
    private final S3Template s3Template;

    public String uploadFile(FileCreateDto fileCreateDto) {

        try (InputStream inputStream = fileCreateDto.getFileList().get(0).getInputStream()) {
            S3Resource s3Resource = s3Template.upload(
                    s3BucketProperties.getBucket(),
                    generateS3Key(fileCreateDto.getS3Folder(), fileCreateDto.getFileList().get(0).getOriginalFilename()),
                    inputStream);

            return s3Resource.getURL().toExternalForm();
        } catch (IOException e) {
            log.error("Upload file error", e);
            throw new ImageFileException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    public List<String> uploadFileList(FileCreateDto fileCreateDto) {
        List<String> uploadedUrlList = new ArrayList<>();

        for (MultipartFile file : fileCreateDto.getFileList()) {
            try (InputStream inputStream = file.getInputStream()) {
                S3Resource s3Resource = s3Template.upload(
                        s3BucketProperties.getBucket(),
                        generateS3Key(fileCreateDto.getS3Folder(), file.getOriginalFilename()),
                        inputStream
                );
                uploadedUrlList.add(s3Resource.getURL().toExternalForm());
            } catch (IOException e) {
                log.error("Upload file list error : ", e);
                throw new ImageFileException(ErrorStatus._INTERNAL_SERVER_ERROR);
            }
        }

        return uploadedUrlList;
    }

    public void deleteFile(String fileUrl) {

        s3Template.deleteObject(fileUrl);
    }

    private String generateS3Key(S3Folder s3Folder, String fileName) {
        if (!StringUtils.hasText(fileName)) {
            log.debug("filename is empty");
            throw new ImageFileException(ErrorStatus._BAD_REQUEST);
        }

        String extension = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) { // 확장자가 있는 경우
            extension = fileName.substring(dotIndex);
        }

        String uniqueFileName = UUID.randomUUID() + extension;
        return s3Folder.getPath() + uniqueFileName;
    }
}
