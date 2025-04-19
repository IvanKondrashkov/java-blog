package ru.yandex.practicum.service;

import java.io.ByteArrayInputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import ru.yandex.practicum.config.properties.AwsProperties;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.exception.S3ConnectionException;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3Client;
    private final AwsProperties awsProperties;

    @Override
    public String uploadImage(String fileName, MultipartFile image) {
        try {
            byte[] bytes = image.getBytes();
            InputStream is = new ByteArrayInputStream(bytes);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            s3Client.putObject(awsProperties.getBucketName(), fileName, is, metadata);
        } catch (IOException e) {
            throw new S3ConnectionException(e.getMessage(), e);
        }
        return s3Client.getUrl(awsProperties.getBucketName(), fileName).toExternalForm();
    }

    @Override
    public byte[] downloadImage(String fileName) {
        try {
            return s3Client.getObject(awsProperties.getBucketName(), fileName).getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new S3ConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(String fileName) {
        s3Client.deleteObject(awsProperties.getBucketName(), fileName);
    }
}