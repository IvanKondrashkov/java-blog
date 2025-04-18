package ru.yandex.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.dto.TagInfo;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class TagMapper {
    public static TagInfo tagToTagInfo(Tag tag) {
        return TagInfo.builder()
                .id(tag.getId())
                .name(tag.getName())
                .postId(tag.getPostId())
                .build();
    }
}