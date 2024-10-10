package com.example.storeme.global.config.s3;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3BucketService {
    private final AmazonS3BucketProperties s3BucketProperties;
    private final S3Template s3Template;

    public String upload(FileCreate fileCreate) {

        try (InputStream inputStream = fileCreate.getFile().getInputStream()) {
            S3Resource s3Resource = s3Template.upload(
                    s3BucketProperties.getBucket(),
                    "user-profile/"+fileCreate.getFile().getOriginalFilename(),
                    inputStream);

            return s3Resource.getURL().toExternalForm();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
