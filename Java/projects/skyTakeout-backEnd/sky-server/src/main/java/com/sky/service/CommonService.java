package com.sky.service;

import org.springframework.web.multipart.MultipartFile;

public interface CommonService {

    /**
     * 文件上传
     * @param file
     * @return
     */
    String upload(MultipartFile file);
}
