package ru.yandex.practicum.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.yandex.practicum.dto.Page;
import ru.yandex.practicum.dto.Sort;
import ru.yandex.practicum.dto.Order;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.dto.PostInfo;
import ru.yandex.practicum.service.PostService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public String redirectToPosts() {
        return "redirect:/posts";
    }

    @GetMapping("/posts/add")
    public String addForm() {
        return "post-add";
    }

    @GetMapping("/posts/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        PostInfo postInfo = postService.findById(id);
        model.addAttribute("post", PostMapper.postInfoToPost(postInfo));
        model.addAttribute("imageUrl", postInfo.getImage().getImageUrl());
        return "post-add";
    }

    @GetMapping("/posts/{id}")
    public String findById(@PathVariable Long id, Model model) {
        PostInfo postInfo = postService.findById(id);
        model.addAttribute("post", postInfo);
        return "post-view";
    }

    @GetMapping("/posts")
    public String findAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "PUBLISH_DT") Sort sort,
            @RequestParam(defaultValue = "DESC") Order order,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model)
    {
        Integer total = search.isEmpty()
                ? postService.count()
                : postService.countByTag(search);

        Integer offset = (pageNumber - 1) * pageSize;
        Boolean hasNext = (offset + pageSize) < total;
        Boolean hasPrevious = pageNumber > 1;

        Page paging = new Page(pageNumber, pageSize, offset, hasNext, hasPrevious);

        List<PostInfo> posts = search.isEmpty()
                ? postService.findAll(sort, order, paging)
                : postService.findAllByTag(sort, order, paging, search);


        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("posts", posts);
        model.addAttribute("paging", paging);
        model.addAttribute("search", search);
        return "posts";
    }

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String save(Post post, Model model) {
        PostInfo postInfo = postService.save(post);
        model.addAttribute("post", postInfo);
        return "redirect:/posts/" + postInfo.getId();
    }

    @PostMapping(value = "/posts/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String update(Post post, @PathVariable Long id) {
        PostInfo postInfo = postService.update(post, id);
        return "redirect:/posts/" + postInfo.getId();
    }

    @PostMapping("/posts/{id}/delete")
    public String deleteById(@PathVariable Long id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}/like")
    public String addLike(@PathVariable Long id, @RequestParam Boolean like) {
        if (like) {
            postService.addLike(id);
        } else {
            postService.deleteLike(id);
        }
        return "redirect:/posts/" + id;
    }
}