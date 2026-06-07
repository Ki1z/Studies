package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.exception.UploadFileFailedException;
import com.sky.exception.UploadFileNotFoundException;
import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        if (file == null)
            throw new UploadFileNotFoundException(MessageConstant.UPLOAD_FILE_NOT_FOUND);

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        // 将空格替换为下划线
        fileName = fileName.replaceAll("\\s+", "_");

        // 文件名长度验证
        if (fileName.length() > 255)
            throw new UploadFileFailedException(MessageConstant.FILE_NAME_TOO_LONG);

        // 上传文件
        String url;
        try {
            url = aliOssUtil.putObject(fileName, file.getBytes());
        } catch (Exception e) {
            throw new UploadFileFailedException(MessageConstant.UPLOAD_FILE_FAILED);
        }
        if (url == null)
            throw new UploadFileFailedException(MessageConstant.UPLOAD_FILE_FAILED);

        return url;
    }
}
