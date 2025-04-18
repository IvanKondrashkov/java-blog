package ru.yandex.practicum.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.Order;
import ru.yandex.practicum.dto.Page;
import ru.yandex.practicum.dto.Sort;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.dto.PostInfo;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.repository.ImageRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.exception.EntityNotFoundException;
import ru.yandex.practicum.repository.TagRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional(readOnly = true)
    public PostInfo findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        Image image = imageRepository.getPostImage(post).orElseThrow(
                () -> new EntityNotFoundException("Image not found!")
        );
        return PostMapper.postToPostInfo(post, tagRepository.getPostTags(post), image);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostInfo> findAll(Sort sort, Order order, Page page) {
        return postRepository.findAll(sort, order, page).stream()
                .map(post -> PostMapper.postToPostInfo(post, tagRepository.getPostTags(post), imageRepository.getPostImage(post).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostInfo> findAllByTag(Sort sort, Order order, Page page, String tag) {
        return postRepository.findAllByTag(sort, order, page, tag).stream()
                .map(post -> PostMapper.postToPostInfo(post, tagRepository.getPostTags(post), imageRepository.getPostImage(post).orElse(null)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer count() {
        return postRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countByTag(String tag) {
        return postRepository.countByTag(tag);
    }

    @Override
    public PostInfo save(Post post) {
        return PostMapper.postToPostInfo(postRepository.save(post));
    }

    @Override
    public PostInfo update(Post post, Long id) {
        postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        return PostMapper.postToPostInfo(postRepository.update(post));
    }

    @Override
    public void deleteById(Long id) {
        postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        postRepository.deleteById(id);
    }

    @Override
    public void addLike(Long id) {
        postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        postRepository.addLike(id);
    }

    @Override
    public void deleteLike(Long id) {
        postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Post not found!")
        );
        postRepository.deleteLike(id);
    }
}