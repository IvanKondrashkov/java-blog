package ru.yandex.practicum.dto;

import lombok.*;
import java.util.Set;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostInfo {
    private Long id;
    private String title;
    private List<String> textByParagraph;
    private Long likes;
    private ImageInfo image;
    private Set<TagInfo> tags;
    private Set<CommentInfo> comments;


    public String getTextPreview() {
        return !textByParagraph.isEmpty() ? textByParagraph.get(0) : "";
    }
}