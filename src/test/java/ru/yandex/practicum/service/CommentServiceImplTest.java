package ru.yandex.practicum.service;

import java.util.List;
import java.util.Optional;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.dto.CommentInfo;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.CommentRepository;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .title("Linux")
                .text("Linux is the best os!")
                .tags("os it soft")
                .build();
        comment = Comment.builder()
                .id(1L)
                .text("Cool os!")
                .build();
    }

    @AfterEach
    void tearDown() {
        post = null;
        comment = null;
    }

    @Test
    void findById() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentInfo commentDb = commentService.findById(post.getId(), comment.getId());

        assertNotNull(commentDb);
        assertEquals(commentDb.getText(), comment.getText());

        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findById(comment.getId());
    }

    @Test
    void findAll() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findAll(post.getId())).thenReturn(List.of(comment));

        List<CommentInfo> comments = commentService.findAll(post.getId());

        assertNotNull(comments);
        assertEquals(comments.size(), 1);

        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findAll(post.getId());
    }

    @Test
    void save() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.save(comment, post.getId())).thenReturn(comment);

        CommentInfo commentDb = commentService.save(comment, post.getId());

        assertNotNull(commentDb);
        assertEquals(commentDb.getText(), comment.getText());

        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).save(comment, post.getId());
    }

    @Test
    void update() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));
        when(commentRepository.update(comment, post.getId())).thenReturn(comment);

        CommentInfo commentDb = commentService.update(comment, post.getId(), comment.getId());

        assertNotNull(commentDb);
        assertEquals(commentDb.getText(), comment.getText());


        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).findById(comment.getId());
        verify(commentRepository, times(1)).update(comment, post.getId());
    }

    @Test
    void deleteById() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        assertDoesNotThrow(() -> commentService.deleteById(post.getId(), comment.getId()));

        verify(postRepository, times(1)).findById(post.getId());
        verify(commentRepository, times(1)).deleteById(comment.getId());
    }
}