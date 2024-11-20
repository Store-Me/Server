package com.example.storeme.global.config.s3.service;

import com.example.storeme.global.config.s3.constant.S3Folder;
import com.example.storeme.global.config.s3.dto.FileCreateDto;
import com.example.storeme.global.config.s3.dto.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final AmazonS3BucketService s3BucketService;

    public String uploadImageFile(S3Folder s3Folder, MultipartFile imageFile){
        if (imageFile == null || imageFile.isEmpty()) {
            log.debug("file is empty");
            return null;
        }

        FileUploadRequest fileUploadRequest = FileUploadRequest.of(s3Folder, imageFile);

        return s3BucketService.uploadFile(fileUploadRequest.toModel());
    }

    public List<String> uploadImageFileList(S3Folder s3Folder, List<MultipartFile> imageFileList){

        if (imageFileList == null) {
            log.debug("file is empty");
            return null;
        }

        FileUploadRequest fileUploadRequest = FileUploadRequest.of(s3Folder, imageFileList);

        return s3BucketService.uploadFileList(fileUploadRequest.toModel());
    }

    public void deleteImageFile(String imageFileUrl){
        if(imageFileUrl.isEmpty())
            return;
        s3BucketService.deleteFile(imageFileUrl);
    }

}
