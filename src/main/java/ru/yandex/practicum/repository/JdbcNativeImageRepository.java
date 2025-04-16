package ru.yandex.practicum.repository;

import java.sql.*;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Image;
import ru.yandex.practicum.service.S3Service;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
@RequiredArgsConstructor
public class JdbcNativeImageRepository implements ImageRepository {
    private final JdbcTemplate jdbcTemplate;
    private final S3Service s3Service;

    @Override
    public void save(Image image, Long postId) {
        final String sqlQuery = "INSERT INTO images(file_name, image_url, publish_dt, post_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"image_id"});
            stmt.setString(1, image.getFileName());
            stmt.setString(2, image.getImageUrl());
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setLong(4, postId);
            return stmt;
        }, keyHolder);
        image.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Image image, Long postId) {
        final String sqlQuery = "UPDATE images SET file_name = ?, image_url = ?, publish_dt = ?, post_id = ? WHERE image_id = ?";
        jdbcTemplate.update(sqlQuery,
                image.getFileName(),
                image.getImageUrl(),
                LocalDateTime.now(),
                postId,
                image.getId()
        );
    }

    @Override
    public void setPostImage(Post post) {
        if (post.getImage() == null) {
            return;
        }

        getPostImage(post).ifPresentOrElse(
                newImage -> {
                    if (post.getImage().isEmpty()) {
                        update(newImage, post.getId());
                    } else {
                        s3Service.deleteImage(newImage.getFileName());
                        final String fileName = UUID.randomUUID().toString();
                        final String imageUrl = s3Service.uploadImage(fileName, post.getImage());

                        newImage.setFileName(fileName);
                        newImage.setImageUrl(imageUrl);
                        update(newImage, post.getId());
                    }
                }, () -> {
                    final String fileName = UUID.randomUUID().toString();
                    final String imageUrl = s3Service.uploadImage(fileName, post.getImage());
                    final Image image = Image.builder()
                            .fileName(fileName)
                            .imageUrl(imageUrl)
                            .publishDt(LocalDateTime.now())
                            .postId(post.getId())
                            .build();

                    save(image, post.getId());
                }
        );
    }

    @Override
    public Optional<Image> getPostImage(Post post) {
        try {
            final String sqlQuery = "SELECT image_id, file_name, image_url, publish_dt, post_id FROM images WHERE post_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToImage, post.getId()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public byte[] getPostImageByte(Post post) {
        final String sqlQuery = "SELECT image_id, file_name, image_url, publish_dt, post_id FROM images WHERE post_id = ?";
        Image image = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToImage, post.getId());
        return s3Service.downloadImage(image.getFileName());
    }

    private Image mapRowToImage(ResultSet rs, int rowNum) throws SQLException {
        return Image.builder()
                .id(rs.getLong("image_id"))
                .fileName(rs.getString("file_name"))
                .imageUrl(rs.getString("image_url"))
                .publishDt(rs.getTimestamp("publish_dt").toLocalDateTime())
                .postId(rs.getLong("post_id"))
                .build();
    }
}