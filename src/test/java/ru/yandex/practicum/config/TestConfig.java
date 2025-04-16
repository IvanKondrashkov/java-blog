package ru.yandex.practicum.config;

import javax.sql.DataSource;
import ru.yandex.practicum.repository.*;
import com.amazonaws.services.s3.AmazonS3;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.yandex.practicum.service.S3Service;
import ru.yandex.practicum.service.S3ServiceImpl;

@Configuration
@Import({DataSourceConfig.class, ThymeleafConfig.class, S3Config.class})
@PropertySource("classpath:application-test.properties")
public class TestConfig {
    @Bean
    @Primary
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("root");
        container.start();
        return container;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        ds.setUsername(postgreSQLContainer.getUsername());
        ds.setPassword(postgreSQLContainer.getPassword());
        ds.setDriverClassName(postgreSQLContainer.getDriverClassName());
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    S3Service s3Service(AmazonS3 s3Client) {
        return new S3ServiceImpl(s3Client);
    }

    @Bean
    TagRepository tagRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativeTagRepository(jdbcTemplate);
    }

    @Bean
    ImageRepository imageRepository(JdbcTemplate jdbcTemplate, S3Service s3Service) {
        return new JdbcNativeImageRepository(jdbcTemplate, s3Service);
    }

    @Bean
    CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativeCommentRepository(jdbcTemplate);
    }

    @Bean
    PostRepository postRepository(
            JdbcTemplate jdbcTemplate,
            TagRepository tagRepository,
            ImageRepository imageRepository,
            CommentRepository commentRepository
    ) {
        return new JdbcNativePostRepository(jdbcTemplate, tagRepository, imageRepository, commentRepository);
    }
}