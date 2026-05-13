package com.eiousee.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aliyun.oss2")
public class AliyunOSS2Properties {
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    private String region;
    private String bucket;

    @Override
    public String toString() {
        return "AliyunOSS2Properties{" +
                "region='" + region + '\'' +
                ", bucket='" + bucket + '\'' +
                '}';
    }
}