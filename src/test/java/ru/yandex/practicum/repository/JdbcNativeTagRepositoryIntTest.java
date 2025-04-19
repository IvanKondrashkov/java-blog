package ru.yandex.practicum.repository;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.model.Post;
import static org.junit.jupiter.api.Assertions.*;

public class JdbcNativeTagRepositoryIntTest extends BaseJdbcNativeRepositoryIntTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private TagRepository tagRepository;
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
    void setPostTags() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        post.setTags("cool profi");
        tagRepository.setPostTags(post);

        Set<Tag> tags = tagRepository.getPostTags(post);

        assertNotNull(tags);
        assertEquals(tags.size(), 5);
    }

    @Test
    void getPostTags() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        Set<Tag> tags = tagRepository.getPostTags(post);

        assertNotNull(tags);
        assertEquals(tags.size(), 3);
    }
}