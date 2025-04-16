package ru.yandex.practicum.repository;

import java.sql.*;
import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
@RequiredArgsConstructor
public class JdbcNativeTagRepository implements TagRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void setPostTags(Post post) {
        if (post.getTags() == null) {
            return;
        }

        final List<String> tags = Arrays.stream(post.getTags().split(" ")).distinct().collect(Collectors.toList());
        final String sqlQuery = "INSERT INTO tags(name, publish_dt, post_id) VALUES (?, ?, ?) ON CONFLICT (name, post_id) DO NOTHING";
        jdbcTemplate.batchUpdate(sqlQuery, tags, 100, (PreparedStatement ps, String tag) -> {
            ps.setString(1, tag);
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(3, post.getId());
        });
    }

    @Override
    public Set<Tag> getPostTags(Post post) {
        final String sqlQuery = "SELECT tag_id, name, publish_dt, post_id FROM tags WHERE post_id = ? ORDER BY publish_dt DESC";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToTag, post.getId()));
    }

    private Tag mapRowToTag(ResultSet rs, int rowNum) throws SQLException {
        return Tag.builder()
                .id(rs.getLong("tag_id"))
                .name(rs.getString("name"))
                .publishDt(rs.getTimestamp("publish_dt").toLocalDateTime())
                .postId(rs.getLong("post_id"))
                .build();
    }
}