package ru.yandex.practicum.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadImage(String fileName, MultipartFile image);
    byte[] downloadImage(String fileName);
    void deleteImage(String fileName);
}