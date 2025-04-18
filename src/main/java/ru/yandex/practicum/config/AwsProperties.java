package ru.yandex.practicum.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AwsProperties {
    @Value("${aws.service.endpoint}")
    private String serviceEndpoint;
    @Value("${aws.region}")
    private String signingRegion;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    @Value("${aws.access.key}")
    private String accessKey;
    @Value("${aws.secret.key}")
    private String secretKey;
}