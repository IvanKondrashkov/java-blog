package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.exception.S3ConnectionException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final AmazonS3 s3Client;
    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Override
    public String uploadImage(String fileName, MultipartFile image) {
        try (var is = image.getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            metadata.setContentType(image.getContentType());

            s3Client.putObject(bucketName, fileName, is, metadata);
        } catch (IOException e) {
            throw new S3ConnectionException(e.getMessage(), e);
        }
        return s3Client.getUrl(bucketName, fileName).toExternalForm();
    }

    @Override
    public byte[] downloadImage(String fileName) {
        try {
            return s3Client.getObject(bucketName, fileName).getObjectContent().readAllBytes();
        } catch (IOException e) {
            throw new S3ConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteImage(String fileName) {
        s3Client.deleteObject(bucketName, fileName);
    }
}