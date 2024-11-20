package com.example.storeme.global.config.s3.dto;

import com.example.storeme.global.config.s3.constant.S3Folder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class FileCreateDto {

    private final S3Folder s3Folder;

    private final List<MultipartFile> fileList;

}
