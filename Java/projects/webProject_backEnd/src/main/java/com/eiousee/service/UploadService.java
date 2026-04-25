package com.eiousee.service;

import com.eiousee.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Result putObject(MultipartFile file);
}
