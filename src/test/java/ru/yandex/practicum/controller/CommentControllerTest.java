package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.dto.CommentInfo;
import ru.yandex.practicum.service.CommentService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CommentService commentService;
    private CommentInfo commentInfo;

    @BeforeEach
    void setUp() {
        commentInfo = CommentInfo.builder()
                .id(1L)
                .text("Coll post!")
                .postId(1L)
                .build();
    }

    @AfterEach
    void tearDown() {
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

        verify(commentService, times(1)).save(any(Comment.class), anyLong());
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

        verify(commentService, times(1)).update(any(Comment.class), anyLong(), anyLong());
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(
                post("/posts/1/comments/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }
}