package ru.yandex.practicum.repository;

import java.util.List;
import java.util.Optional;

import ru.yandex.practicum.dto.Order;
import ru.yandex.practicum.dto.Page;
import ru.yandex.practicum.dto.Sort;
import ru.yandex.practicum.model.Post;

public interface PostRepository {
    Optional<Post> findById(Long id);
    List<Post> findAll(Sort sort, Order order, Page page);
    List<Post> findAllByTag(Sort sort, Order order, Page page, String tag);
    Integer count();
    Integer countByTag(String tag);
    Post save(Post post);
    Post update(Post post);
    void deleteById(Long id);
    void deleteAll();
    void addLike(Long id);
    void deleteLike(Long id);
}