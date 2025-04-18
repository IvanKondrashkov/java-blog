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
public class Tag {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    private LocalDateTime publishDt;
    @NotNull
    private Long postId;
}