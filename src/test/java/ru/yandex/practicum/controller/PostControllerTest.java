package ru.yandex.practicum.controller;

import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.dto.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PostService postService;
    private PostInfo postInfo;
    private ImageInfo imageInfo;
    private Set<TagInfo> tags;

    @BeforeEach
    void setUp() {
        tags = Set.of(
                TagInfo.builder().id(1L).name("os").postId(1L).build(),
                TagInfo.builder().id(2L).name("it").postId(1L).build(),
                TagInfo.builder().id(3L).name("soft").postId(1L).build()
        );
        imageInfo = ImageInfo.builder()
                .id(1L)
                .imageUrl("http://cloud.ru")
                .postId(1L)
                .build();
        postInfo = PostInfo.builder()
                .id(1L)
                .textByParagraph(List.of("The best post!"))
                .image(imageInfo)
                .tags(tags)
                .comments(Set.of())
                .build();
    }

    @AfterEach
    void tearDown() {
        tags = null;
        imageInfo = null;
        postInfo = null;
    }

    @Test
    void redirectToPosts() throws Exception {
        mockMvc.perform(
                get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void addForm() throws Exception {
        mockMvc.perform(
                get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("post-add"));
    }

    @Test
    void editForm() throws Exception {
        when(postService.findById(1L)).thenReturn(postInfo);

        mockMvc.perform(
                get("/posts/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("post-add"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("imageUrl"));

        verify(postService, times(1)).findById(1L);
    }

    @Test
    void findById() throws Exception {
        when(postService.findById(1L)).thenReturn(postInfo);

        mockMvc.perform(
                get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post-view"))
                .andExpect(model().attributeExists("post"));

        verify(postService, times(1)).findById(1L);
    }

    @Test
    void findAll() throws Exception {
        when(postService.findAll(any(), any(), any())).thenReturn(Collections.singletonList(postInfo));
        when(postService.count()).thenReturn(1);

        mockMvc.perform(
                get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("paging"));

        verify(postService, times(1)).findAll(any(), any(), any());
        verify(postService, times(1)).count();
    }

    @Test
    void findAllByTag() throws Exception {
        when(postService.findAllByTag(any(), any(), any(), anyString())).thenReturn(Collections.singletonList(postInfo));
        when(postService.countByTag(anyString())).thenReturn(1);

        mockMvc.perform(
                get("/posts")
                        .param("search", "it"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(model().attributeExists("search"));

        verify(postService, times(1)).findAllByTag(any(), any(), any(), anyString());
        verify(postService, times(1)).countByTag(anyString());
    }

    @Test
    void save() throws Exception {
        when(postService.save(any(Post.class))).thenReturn(postInfo);

        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("title", postInfo.getTitle())
                        .param("text", postInfo.getTextPreview())
                        .param("tags", tags.stream().map(TagInfo::getName).collect(Collectors.joining(" "))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postService, times(1)).save(any(Post.class));
    }

    @Test
    void update() throws Exception {
        when(postService.update(any(Post.class), any())).thenReturn(postInfo);

        mockMvc.perform(
                post("/posts/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        verify(postService, times(1)).update(any(Post.class), any());
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(
                post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void addLike() throws Exception {
        mockMvc.perform(
                post("/posts/1/like")
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }
}