package ru.yandex.practicum.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentInfo {
    private Long id;
    private String text;
    private Long postId;
}