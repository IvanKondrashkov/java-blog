package ru.yandex.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.dto.CommentInfo;
import ru.yandex.practicum.model.Comment;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CommentMapper {
    public static CommentInfo commentToCommentInfo(Comment comment) {
        return CommentInfo.builder()
                .id(comment.getId())
                .text(comment.getText())
                .postId(comment.getPostId())
                .build();
    }
}