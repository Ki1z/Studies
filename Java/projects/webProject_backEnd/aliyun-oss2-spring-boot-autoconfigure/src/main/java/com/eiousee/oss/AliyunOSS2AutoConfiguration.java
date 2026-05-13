package com.eiousee.oss;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunOSS2AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliyunOSS2Operator aliyunOSS2Operator(AliyunOSS2Properties aliyunOSS2Properties) {
        return new AliyunOSS2Operator(aliyunOSS2Properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunOSS2Properties aliyunOSS2Properties() {
        return new AliyunOSS2Properties();
    }
}
