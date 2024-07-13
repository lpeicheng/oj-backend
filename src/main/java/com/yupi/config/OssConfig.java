package com.yupi.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云文件系统配置
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.oss")
@Data
public class OssConfig {
    private String endpoint;
    private String accessKey;
    private String keySecret;
    private String bucketName;

@Bean
public OSS ossClient() {
    if (endpoint == null || accessKey == null || keySecret == null) {
        throw new IllegalArgumentException("Endpoint, AccessKey or KeySecret cannot be null.");
    }
    return new OSSClientBuilder().build(endpoint, accessKey, keySecret);
}
}