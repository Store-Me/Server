package com.example.storeme.global.config.s3;

import com.example.storeme.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileUploadController {
    private final FileService fileService;

    @PostMapping
    public ResponseDto<?> fileUpload(@RequestPart MultipartFile file) {
        FileUploadRequest fileUploadRequest = FileUploadRequest.of(file);
        String fileUrl = fileService.upload(fileUploadRequest.toModel());
        return ResponseDto.onSuccess(fileUrl);
    }
}
