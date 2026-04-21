package com.eiousee.service.impl;

import com.eiousee.pojo.Result;
import com.eiousee.service.UploadService;
import com.eiousee.utils.AliyunOSS2Operator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    private final AliyunOSS2Operator aliyunOSS2Operator;

    public UploadServiceImpl(AliyunOSS2Operator aliyunOSS2Operator) {
        this.aliyunOSS2Operator = aliyunOSS2Operator;
    }

    @Override
    public Result putObject(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String url;
        try {
            url = aliyunOSS2Operator.putObject(fileName, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (url != null) {
            return Result.success(url);
        } else {
            return Result.error("上传失败");
        }
    }
}
