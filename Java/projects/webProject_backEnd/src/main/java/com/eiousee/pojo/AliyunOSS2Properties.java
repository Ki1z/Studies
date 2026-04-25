package com.eiousee.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss2")
public class AliyunOSS2Properties {
    private String region;
    private String bucket;
}