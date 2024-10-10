package com.example.storeme.global.config.s3;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class FileCreate {

    private final MultipartFile file;

}
