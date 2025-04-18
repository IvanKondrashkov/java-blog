package ru.yandex.practicum.service;

import java.util.List;
import ru.yandex.practicum.dto.Page;
import ru.yandex.practicum.dto.Sort;
import ru.yandex.practicum.dto.Order;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.dto.PostInfo;

public interface PostService {
    PostInfo findById(Long id);
    List<PostInfo> findAll(Sort sort, Order order, Page page);
    List<PostInfo> findAllByTag(Sort sort, Order order, Page page, String tag);
    Integer count();
    Integer countByTag(String tag);
    PostInfo save(Post post);
    PostInfo update(Post post, Long id);
    void deleteById(Long id);
    void addLike(Long id);
    void deleteLike(Long id);
}