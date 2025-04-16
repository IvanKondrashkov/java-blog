package ru.yandex.practicum.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagInfo {
    private Long id;
    private String name;
    private Long postId;
}