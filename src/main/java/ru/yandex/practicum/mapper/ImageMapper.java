package ru.yandex.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.dto.ImageInfo;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ImageMapper {
    public static ImageInfo imageToImageInfo(Image image) {
        return ImageInfo.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .postId(image.getPostId())
                .build();
    }
}