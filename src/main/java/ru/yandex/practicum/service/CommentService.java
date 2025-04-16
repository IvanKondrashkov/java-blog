package ru.yandex.practicum.service;

import java.util.List;

import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.dto.CommentInfo;

public interface CommentService {
    CommentInfo findById(Long postId, Long id);
    List<CommentInfo> findAll(Long postId);
    CommentInfo save(Comment comment, Long postId);
    CommentInfo update(Comment comment, Long postId, Long id);
    void deleteById(Long postId, Long id);
}