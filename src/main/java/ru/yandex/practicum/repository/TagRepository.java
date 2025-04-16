package ru.yandex.practicum.repository;

import java.util.Set;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.model.Post;

public interface TagRepository {
    void setPostTags(Post post);
    Set<Tag> getPostTags(Post post);
}