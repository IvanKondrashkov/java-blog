package ru.yandex.practicum.controller;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.config.TestConfig;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.dto.CommentInfo;
import ru.yandex.practicum.service.CommentService;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class CommentControllerTest {
    @Mock
    private CommentService commentService;
    @InjectMocks
    private CommentController commentController;
    private MockMvc mockMvc;
    private CommentInfo commentInfo;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        commentInfo = CommentInfo.builder()
                .id(1L)
                .text("Coll post!")
                .postId(1L)
                .build();
    }

    @AfterEach
    void tearDown() {
        mockMvc = null;
        commentInfo = null;
    }

    @Test
    void save() throws Exception {
        when(commentService.save(any(Comment.class), anyLong())).thenReturn(commentInfo);

        mockMvc.perform(
                post("/posts/1/comments")
                        .param("text", commentInfo.getText()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void update() throws Exception {
        commentInfo.setText("Bad post!");
        when(commentService.update(any(Comment.class), anyLong(), anyLong())).thenReturn(commentInfo);

        mockMvc.perform(
                post("/posts/1/comments/1")
                        .param("text", commentInfo.getText()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(
                post("/posts/1/comments/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }
}