package ru.yandex.practicum.repository;

import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import com.amazonaws.services.s3.AmazonS3;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Image;
import static org.junit.jupiter.api.Assertions.*;

public class JdbcNativeImageRepositoryIntTest extends BaseJdbcNativeRepositoryIntTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private AmazonS3 s3Client;
    private Post post;

    @BeforeEach
    void setUp() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/test-image.jpg");
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());

        s3Client.createBucket("test-bucket");

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