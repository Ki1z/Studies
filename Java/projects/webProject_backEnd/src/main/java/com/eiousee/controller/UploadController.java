package com.eiousee.controller;

import com.eiousee.pojo.Result;
import com.eiousee.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping
    public Result upload(MultipartFile file) {
        log.info("上传文件：{}", file.getOriginalFilename());
        return uploadService.putObject(file);
    }
}
