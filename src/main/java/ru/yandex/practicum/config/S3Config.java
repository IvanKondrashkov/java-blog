package ru.yandex.practicum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
    @Bean
    public AmazonS3 s3Client(
            @Value("${aws.service.endpoint}") String serviceEndpoint,
            @Value("${aws.region}") String signingRegion,
            @Value("${aws.access.key}") String accessKey,
            @Value("${aws.secret.key}") String secretKey
    ) {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }
}