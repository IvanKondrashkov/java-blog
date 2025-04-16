package ru.yandex.practicum.model;

import lombok.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
    private Long id;
    @NotNull
    @NotBlank
    private String text;
    private LocalDateTime publishDt;
    @NotNull
    private Long postId;
}