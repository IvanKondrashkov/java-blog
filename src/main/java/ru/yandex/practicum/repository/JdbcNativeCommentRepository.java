package ru.yandex.practicum.repository;

import java.sql.*;
import java.util.Set;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Comment;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
@RequiredArgsConstructor
public class JdbcNativeCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Comment> findById(Long id) {
        try {
            final String sqlQuery = "SELECT comment_id, text, publish_dt, post_id FROM comments WHERE comment_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToComment, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Comment> findAll(Long postId) {
        final String sqlQuery = "SELECT comment_id, text, publish_dt, post_id FROM comments WHERE post_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToComment, postId);
    }

    @Override
    public Comment save(Comment comment, Long postId) {
        final String sqlQuery = "INSERT INTO comments(text, publish_dt, post_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"comment_id"});
            stmt.setString(1, comment.getText());
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(3, postId);
            return stmt;
        }, keyHolder);
        comment.setId(keyHolder.getKey().longValue());
        return comment;
    }

    @Override
    public Comment update(Comment comment, Long postId) {
        final String sqlQuery = "UPDATE comments SET text = ?, publish_dt = ?, post_id = ? WHERE comment_id = ?";
        jdbcTemplate.update(sqlQuery,
                comment.getText(),
                LocalDateTime.now(),
                postId,
                comment.getId()
        );
        return findById(comment.getId()).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        final String sqlQuery = "DELETE FROM comments WHERE comment_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Set<Comment> getPostComments(Post post) {
        final String sqlQuery = "SELECT comment_id, text, publish_dt, post_id FROM comments WHERE post_id = ? ORDER BY publish_dt DESC";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToComment, post.getId()));
    }

    private Comment mapRowToComment(ResultSet rs, int rowNum) throws SQLException {
        return Comment.builder()
                .id(rs.getLong("comment_id"))
                .text(rs.getString("text"))
                .publishDt(rs.getTimestamp("publish_dt").toLocalDateTime())
                .postId(rs.getLong("post_id"))
                .build();
    }
}