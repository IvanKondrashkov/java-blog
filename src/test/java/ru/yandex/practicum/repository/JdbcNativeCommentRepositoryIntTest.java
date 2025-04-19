package ru.yandex.practicum.repository;

import java.util.Set;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Comment;
import static org.junit.jupiter.api.Assertions.*;


public class JdbcNativeCommentRepositoryIntTest extends BaseJdbcNativeRepositoryIntTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .title("Linux")
                .text("Linux is the best os!")
                .tags("os it soft")
                .build();
        comment = Comment.builder()
                .text("Cool os!")
                .build();
    }

    @AfterEach
    void tearDown() {
        post = null;
        comment = null;
        postRepository.deleteAll();
    }

    @Test
    void findById() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        comment = commentRepository.save(comment, post.getId());

        assertNotNull(comment.getId());

        Comment commentDb = commentRepository.findById(comment.getId()).orElse(null);

        assertNotNull(commentDb);
        assertEquals(commentDb.getText(), comment.getText());
        assertNotNull(commentDb.getPublishDt());
    }

    @Test
    void findAll() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        comment = commentRepository.save(comment, post.getId());

        assertNotNull(comment.getId());

        List<Comment> comments = commentRepository.findAll(comment.getId());

        assertNotNull(comments);
        assertEquals(comments.size(), 1);
    }

    @Test
    void save() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        comment = commentRepository.save(comment, post.getId());

        assertNotNull(comment.getId());
    }

    @Test
    void update() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        comment = commentRepository.save(comment, post.getId());

        assertNotNull(comment.getId());

        comment.setText("Bad os!");
        Comment commentDb = commentRepository.update(comment, post.getId());

        assertNotNull(commentDb.getId());
        assertEquals(commentDb.getText(), "Bad os!");
    }

    @Test
    void deleteById() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        comment = commentRepository.save(comment, post.getId());

        assertNotNull(comment.getId());

        commentRepository.deleteById(comment.getId());
        Comment commentDb = commentRepository.findById(comment.getId()).orElse(null);

        assertNull(commentDb);
    }

    @Test
    void getPostComments() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        comment = commentRepository.save(comment, post.getId());

        assertNotNull(comment.getId());

        Set<Comment> comments = commentRepository.getPostComments(post);

        assertNotNull(comments);
        assertEquals(comments.size(), 1);
    }
}