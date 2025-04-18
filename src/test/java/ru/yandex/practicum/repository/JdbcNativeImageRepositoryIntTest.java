package ru.yandex.practicum.repository;

import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.config.TestConfig;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Testcontainers
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class JdbcNativeImageRepositoryIntTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;
    private Post post;

    @BeforeEach
    void setUp() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/test-image.jpg");
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());

        post = Post.builder()
                .title("Linux")
                .text("Linux is the best os!")
                .image(new MockMultipartFile("image", bytes))
                .tags("os it soft")
                .build();
    }

    @AfterEach
    void tearDown() {
        post = null;
    }

    @Test
    void save() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        Image imageDb = imageRepository.getPostImage(post).orElse(null);

        assertNotNull(imageDb);
        assertNotNull(imageDb.getId());
    }

    @Test
    void update() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        Image imageDb = imageRepository.getPostImage(post).orElse(null);

        assertNotNull(imageDb);
        assertNotNull(imageDb.getId());

        imageRepository.update(imageDb, post.getId());

        imageDb = imageRepository.getPostImage(post).orElse(null);
        assertNotNull(imageDb);
        assertNotNull(imageDb.getId());
    }

    @Test
    void getPostImage() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        Image imageDb = imageRepository.getPostImage(post).orElse(null);

        assertNotNull(imageDb);
        assertNotNull(imageDb.getId());
    }

    @Test
    void getPostImageByte() {
        post = postRepository.save(post);

        assertNotNull(post.getId());

        byte[] bytes = imageRepository.getPostImageByte(post);

        assertTrue(bytes.length > 0);
    }
}