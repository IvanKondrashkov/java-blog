package ru.yandex.practicum.service;

import java.util.List;
import java.util.Set;
import java.util.Optional;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private PostServiceImpl postService;
    private Post post;
    private Set<Tag> tags;


    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .title("Linux")
                .text("Linux is the best os!")
                .tags("os it soft")
                .build();

        tags = Set.of(
                Tag.builder().id(1L).name("os").build(),
                Tag.builder().id(2L).name("it").build(),
                Tag.builder().id(3L).name("soft").build()
        );
    }

    @AfterEach
    void tearDown() {
        post = null;
        tags = null;
    }

    @Test
    void findById() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(imageRepository.getPostImage(post)).thenReturn(Optional.of(Image.builder().build()));
        when(tagRepository.getPostTags(post)).thenReturn(tags);

        PostInfo postDb = postService.findById(post.getId());

        assertNotNull(postDb);
        assertEquals(postDb.getTitle(), post.getTitle());
        assertEquals(postDb.getTextByParagraph().size(), 1);
        assertEquals(postDb.getTags().size(), 3);

        verify(postRepository, times(1)).findById(post.getId());
        verify(imageRepository, times(1)).getPostImage(post);
        verify(tagRepository, times(1)).getPostTags(post);
    }

    @Test
    void findAll() {
        Page page = new Page(1, 5, 0, false, false);
        when(postRepository.findAll(Sort.PUBLISH_DT, Order.DESC, page)).thenReturn(List.of(post));
        when(imageRepository.getPostImage(post)).thenReturn(Optional.of(Image.builder().build()));
        when(tagRepository.getPostTags(post)).thenReturn(tags);

        List<PostInfo> posts = postService.findAll(Sort.PUBLISH_DT, Order.DESC, page);

        assertNotNull(posts);
        assertEquals(posts.size(), 1);

        verify(postRepository, times(1)).findAll(Sort.PUBLISH_DT, Order.DESC, page);
        verify(imageRepository, times(1)).getPostImage(post);
        verify(tagRepository, times(1)).getPostTags(post);
    }

    @Test
    void findAllByTag() {
        Page page = new Page(1, 5, 0, false, false);
        when(postRepository.findAllByTag(Sort.PUBLISH_DT, Order.DESC, page, "os")).thenReturn(List.of(post));
        when(imageRepository.getPostImage(post)).thenReturn(Optional.of(Image.builder().build()));
        when(tagRepository.getPostTags(post)).thenReturn(tags);

        List<PostInfo> posts = postService.findAllByTag(Sort.PUBLISH_DT, Order.DESC, page, "os");

        assertNotNull(posts);
        assertEquals(posts.size(), 1);

        verify(postRepository, times(1)).findAllByTag(Sort.PUBLISH_DT, Order.DESC, page, "os");
        verify(imageRepository, times(1)).getPostImage(post);
        verify(tagRepository, times(1)).getPostTags(post);
    }

    @Test
    void count() {
        when(postRepository.count()).thenReturn(1);

        Integer count = postService.count();

        assertEquals(count, 1);

        verify(postRepository, times(1)).count();
    }

    @Test
    void countByTag() {
        when(postRepository.countByTag("os")).thenReturn(1);

        Integer count = postService.countByTag("os");

        assertEquals(count, 1);

        verify(postRepository, times(1)).countByTag("os");
    }

    @Test
    void save() {
        when(postRepository.save(post)).thenReturn(post);

        PostInfo postDb = postService.save(post);

        assertNotNull(postDb);
        assertEquals(postDb.getTitle(), post.getTitle());
        assertEquals(postDb.getTextByParagraph().size(), 1);

        verify(postRepository, times(1)).save(post);
    }

    @Test
    void update() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.update(post)).thenReturn(post);

        PostInfo postDb = postService.update(post, post.getId());

        assertNotNull(postDb);
        assertEquals(postDb.getTitle(), post.getTitle());
        assertEquals(postDb.getTextByParagraph().size(), 1);

        verify(postRepository, times(1)).findById(postDb.getId());
        verify(postRepository, times(1)).update(post);
    }

    @Test
    void deleteById() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> postService.deleteById(post.getId()));
        verify(postRepository).deleteById(post.getId());

        verify(postRepository, times(1)).findById(post.getId());
    }

    @Test
    void addLike() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> postService.addLike(post.getId()));

        verify(postRepository).addLike(post.getId());
    }

    @Test
    void deleteLike() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        assertDoesNotThrow(() -> postService.deleteLike(post.getId()));

        verify(postRepository).deleteLike(post.getId());
    }
}