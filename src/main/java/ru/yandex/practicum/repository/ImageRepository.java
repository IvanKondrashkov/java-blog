package ru.yandex.practicum.repository;

import java.util.Optional;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Image;

public interface ImageRepository {
    void save(Image image, Long postId);
    void update(Image image, Long postId);
    void setPostImage(Post post);
    Optional<Image> getPostImage(Post post);
    byte[] getPostImageByte(Post post);
}