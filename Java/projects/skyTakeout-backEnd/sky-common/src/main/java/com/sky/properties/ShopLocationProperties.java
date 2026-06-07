package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.shop.location")
@Data
public class ShopLocationProperties {
    // 纬度
    private String latitude;
    // 经度
    private String longitude;
    // 精确地址
    private String address;

    // 高德api key
    private String key;
}
