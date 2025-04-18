package ru.yandex.practicum.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {
    private Long id;
    private String imageUrl;
    private Long postId;
}