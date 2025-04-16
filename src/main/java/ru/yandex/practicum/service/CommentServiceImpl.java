package ru.yandex.practicum.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.dto.CommentInfo;
import ru.yandex.practicum.mapper.CommentMapper;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.exception.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public CommentInfo findById(Long postId, Long id) {
        postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment not found!")
        );
        return CommentMapper.commentToCommentInfo(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentInfo> findAll(Long postId) {
        postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        return commentRepository.findAll(postId).stream()
                .map(CommentMapper::commentToCommentInfo)
                .collect(Collectors.toList());
    }

    @Override
    public CommentInfo save(Comment comment, Long postId) {
        postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        return CommentMapper.commentToCommentInfo(commentRepository.save(comment, postId));
    }

    @Override
    public CommentInfo update(Comment comment, Long postId, Long id) {
        postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment not found!")
        );
        return CommentMapper.commentToCommentInfo(commentRepository.update(comment, postId));
    }

    @Override
    public void deleteById(Long postId, Long id) {
        postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Comment not found!")
        );
        commentRepository.deleteById(id);
    }
}