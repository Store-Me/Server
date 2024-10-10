package com.example.storeme.global.config.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final AmazonS3BucketService amazonS3BucketService;

    public String upload(FileCreate fileCreate) {

        return amazonS3BucketService.upload(fileCreate);
    }
}
