package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.dto.CommentInfo;
import ru.yandex.practicum.service.CommentService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public String save(Comment comment, @PathVariable Long postId) {
        CommentInfo commentInfo = commentService.save(comment, postId);
        return "redirect:/posts/" + commentInfo.getPostId();
    }

    @PostMapping("{postId}/comments/{id}")
    public String update(Comment comment, @PathVariable Long postId, @PathVariable Long id) {
        CommentInfo commentInfo = commentService.update(comment, postId, id);
        return "redirect:/posts/" + commentInfo.getPostId();
    }

    @PostMapping("{postId}/comments/{id}/delete")
    public String deleteById(@PathVariable Long postId, @PathVariable Long id) {
        commentService.deleteById(postId, id);
        return "redirect:/posts/" + postId;
    }
}