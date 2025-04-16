package ru.yandex.practicum.mapper;

import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.stream.Collectors;
import ru.yandex.practicum.dto.TagInfo;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.dto.PostInfo;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class PostMapper {
    public static PostInfo postToPostInfo(Post post) {
        return PostInfo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .textByParagraph(getTextByParagraph(post.getText()))
                .likes(post.getLikes())
                .comments(post.getComments() == null ? new LinkedHashSet<>() : post.getComments().stream()
                        .map(CommentMapper::commentToCommentInfo)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public static PostInfo postToPostInfo(Post post, Set<Tag> tags, Image image) {
        return PostInfo.builder()
                .id(post.getId())
                .title(post.getTitle())
                .textByParagraph(getTextByParagraph(post.getText()))
                .tags(tags.stream()
                        .map(TagMapper::tagToTagInfo)
                        .collect(Collectors.toSet())
                )
                .likes(post.getLikes())
                .image(ImageMapper.imageToImageInfo(image))
                .comments(post.getComments() == null ? new LinkedHashSet<>() : post.getComments().stream()
                        .map(CommentMapper::commentToCommentInfo)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public static Post postInfoToPost(PostInfo postInfo) {
        return Post.builder()
                .id(postInfo.getId())
                .title(postInfo.getTitle())
                .text(String.join(" ", postInfo.getTextByParagraph()))
                .tags(postInfo.getTags().stream()
                        .map(TagInfo::getName)
                        .collect(Collectors.joining(" "))
                )
                .build();
    }

    private static List<String> getTextByParagraph(String text) {
        String[] sentences = text.split("(?<=[.!?])\\s+");
        List<String> paragraphs = new ArrayList<>();

        for (int i = 0; i < sentences.length; i += 3) {
            int end = Math.min(i + 3, sentences.length);
            String paragraph = String.join(" ", Arrays.copyOfRange(sentences, i, end));
            paragraphs.add(paragraph);
        }
        return paragraphs;
    }
}