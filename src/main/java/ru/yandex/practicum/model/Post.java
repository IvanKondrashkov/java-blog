package ru.yandex.practicum.model;

import lombok.*;
import java.util.Set;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Post {
    private Long id;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String text;
    private Long likes;
    private MultipartFile image;
    private String tags;
    private Set<Comment> comments;
    private LocalDateTime publishDt;
}