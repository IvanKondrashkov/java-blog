package ru.yandex.practicum.repository;

import java.util.Set;
import java.util.List;
import java.util.Optional;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Comment;

public interface CommentRepository {
    Optional<Comment> findById(Long id);
    List<Comment> findAll(Long postId);
    Comment save(Comment comment, Long postId);
    Comment update(Comment comment, Long postId);
    void deleteById(Long id);
    Set<Comment> getPostComments(Post post);
}