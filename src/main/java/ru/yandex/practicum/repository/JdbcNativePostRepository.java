package ru.yandex.practicum.repository;

import java.sql.*;
import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.dto.Order;
import ru.yandex.practicum.dto.Page;
import ru.yandex.practicum.dto.Sort;
import ru.yandex.practicum.model.Post;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
@RequiredArgsConstructor
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TagRepository tagRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository commentRepository;

    @Override
    public Optional<Post> findById(Long id) {
        try {
            final String sqlQuery = "SELECT post_id, title, text, publish_dt, likes FROM posts WHERE post_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToPost, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Post> findAll(Sort sort, Order order, Page page) {
        final String sqlQuery = String.format(
                "SELECT post_id, title, text, publish_dt, likes FROM posts ORDER BY %s %s LIMIT ? OFFSET ?",
                Sort.fromValue(sort.getValue()),
                Order.fromValue(order.getValue())
        );
        return jdbcTemplate.query(sqlQuery, this::mapRowToPost, page.getPageSize(), page.getOffset());
    }

    @Override
    public List<Post> findAllByTag(Sort sort, Order order, Page page, String tag) {
        final String sqlQuery = String.format(
                "SELECT p.post_id, p.title, p.text, p.publish_dt, p.likes FROM posts AS p JOIN tags AS t ON t.post_id = p.post_id WHERE t.name ILIKE ? ORDER BY %s %s LIMIT ? OFFSET ?",
                Sort.fromValue(sort.getValue()),
                Order.fromValue(order.getValue())
        );
        return jdbcTemplate.query(sqlQuery, this::mapRowToPost, tag, page.getPageSize(), page.getOffset());
    }

    @Override
    public Integer count() {
        final String sqlQuery = "SELECT COUNT(post_id) FROM posts";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class);
    }

    @Override
    public Integer countByTag(String tag) {
        final String sqlQuery = "SELECT COUNT(p.post_id) FROM posts AS p JOIN tags AS t ON t.post_id = p.post_id WHERE t.name ILIKE ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, tag);
    }

    @Override
    public Post save(Post post) {
        final String sqlQuery = "INSERT INTO posts(title, text, publish_dt) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"post_id"});
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getText());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return stmt;
        }, keyHolder);
        post.setId(keyHolder.getKey().longValue());
        tagRepository.setPostTags(post);
        imageRepository.setPostImage(post);
        return post;
    }

    @Override
    public Post update(Post post) {
        final String sqlQuery = "UPDATE posts SET title = ?, text = ?, publish_dt = ? WHERE post_id = ?";
        jdbcTemplate.update(sqlQuery,
                post.getTitle(),
                post.getText(),
                LocalDateTime.now(),
                post.getId()
        );
        tagRepository.setPostTags(post);
        imageRepository.setPostImage(post);
        return findById(post.getId()).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        final String sqlQuery = "DELETE FROM posts WHERE post_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void deleteAll() {
        final String sqlQuery = "TRUNCATE TABLE posts CASCADE";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public void addLike(Long id) {
        final String sqlQuery = "UPDATE posts SET likes = likes + 1 WHERE post_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void deleteLike(Long id) {
        final String sqlQuery = "UPDATE posts SET likes = likes - 1 WHERE post_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    private Post mapRowToPost(ResultSet rs, int rowNum) throws SQLException {
        Post post = Post.builder()
                .id(rs.getLong("post_id"))
                .title(rs.getString("title"))
                .text(rs.getString("text"))
                .publishDt(rs.getTimestamp("publish_dt").toLocalDateTime())
                .likes(rs.getLong("likes"))
                .comments(new HashSet<>())
                .build();

        post.setComments(commentRepository.getPostComments(post));
        return post;
    }
}