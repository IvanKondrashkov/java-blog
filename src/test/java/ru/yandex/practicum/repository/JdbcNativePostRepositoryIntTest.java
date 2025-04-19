package ru.yandex.practicum.repository;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.dto.Page;
import ru.yandex.practicum.dto.Sort;
import ru.yandex.practicum.dto.Order;
import ru.yandex.practicum.model.Post;
import static org.junit.jupiter.api.Assertions.*;

public class JdbcNativePostRepositoryIntTest extends BaseJdbcNativeRepositoryIntTest {
    @Autowired
    private PostRepository postRepository;
    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .title("Linux")
                .text("Linux is the best os!")
                .tags("os it soft")
                .build();
    }

    @AfterEach
    void tearDown() {
        post = null;
        postRepository.deleteAll();
    }

    @Test
    void findById() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        Post postDb = postRepository.findById(post.getId()).orElse(null);

        assertNotNull(postDb);
        assertEquals(postDb.getTitle(), post.getTitle());
        assertEquals(postDb.getText(), post.getText());
        assertNotNull(postDb.getPublishDt());
        assertEquals(postDb.getLikes(), 0);
    }

    @Test
    void findAll() {
        Page page = new Page(1, 5, 0, false, false);
        post = postRepository.save(post);

        assertNotNull(post.getId());

        List<Post> posts = postRepository.findAll(Sort.PUBLISH_DT, Order.DESC, page);

        assertNotNull(posts);
        assertEquals(posts.size(), 1);
    }

    @Test
    void findAllByTag() {
        Page page = new Page(1, 5, 0, false, false);
        post = postRepository.save(post);

        assertNotNull(post.getId());

        List<Post> posts = postRepository.findAllByTag(Sort.PUBLISH_DT, Order.DESC, page, "os");

        assertNotNull(posts);
        assertEquals(posts.size(), 1);
    }

    @Test
    void count() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        Integer count = postRepository.count();

        assertEquals(count, 1);
    }

    @Test
    void countByTag() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        Integer count = postRepository.countByTag("os");

        assertEquals(count, 1);
    }

    @Test
    void save() {
        post = postRepository.save(post);

        assertNotNull(post.getId());
    }

    @Test
    void update() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        post.setTitle("Windows");
        Post postDb = postRepository.update(post);

        assertNotNull(postDb.getId());
        assertEquals(postDb.getTitle(), "Windows");
    }

    @Test
    void deleteById() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        postRepository.deleteById(post.getId());
        Post postDb = postRepository.findById(post.getId()).orElse(null);

        assertNull(postDb);
    }

    @Test
    void deleteAll() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        postRepository.deleteAll();
        List<Post> posts = postRepository.findAll(Sort.PUBLISH_DT, Order.DESC, new Page(1, 5, 0, false, false));

        assertTrue(posts.isEmpty());
    }

    @Test
    void addLike() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        postRepository.addLike(post.getId());
        Post postDb = postRepository.findById(post.getId()).orElse(null);

        assertNotNull(postDb);
        assertEquals(postDb.getLikes(), 1);
    }

    @Test
    void deleteLike() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        postRepository.addLike(post.getId());
        postRepository.deleteLike(post.getId());
        Post postDb = postRepository.findById(post.getId()).orElse(null);

        assertNotNull(postDb);
        assertEquals(postDb.getLikes(), 0);
    }
}