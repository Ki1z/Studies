package com.eiousee.service.impl;

import com.eiousee.oss.AliyunOSS2Operator;
import com.eiousee.pojo.Result;
import com.eiousee.service.UploadService;
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
        // 将文件名中的空格换成下划线
        fileName = fileName.replaceAll("\\s+", "_");
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
