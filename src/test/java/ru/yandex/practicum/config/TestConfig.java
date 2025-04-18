package ru.yandex.practicum.config;

import javax.sql.DataSource;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import ru.yandex.practicum.repository.*;
import com.amazonaws.services.s3.AmazonS3;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.utility.DockerImageName;
import ru.yandex.practicum.service.S3Service;
import ru.yandex.practicum.service.S3ServiceImpl;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Configuration
@Import({DataSourceConfig.class, ThymeleafConfig.class})
public class TestConfig {
    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14")
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("root");
        container.start();
        return container;
    }

    @Bean
    public LocalStackContainer localStackContainer() {
        LocalStackContainer localStack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:2.3.2"))
                .withServices(S3);
        localStack.start();
        return localStack;
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
    public AmazonS3 s3Client(LocalStackContainer localStack) {
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(localStack.getEndpointOverride(S3).toString(), localStack.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(localStack.getAccessKey(), localStack.getSecretKey())))
                .build();
    }

    @Bean
    public AwsProperties awsProperties(AmazonS3 s3Client, LocalStackContainer localStack) {
        var bucket = s3Client.createBucket("test-bucket");
        var awsProperties = new AwsProperties();

        awsProperties.setServiceEndpoint(localStack.getEndpointOverride(S3).toString());
        awsProperties.setSigningRegion(localStack.getRegion());
        awsProperties.setBucketName(bucket.getName());
        awsProperties.setAccessKey(localStack.getAccessKey());
        awsProperties.setSecretKey(localStack.getSecretKey());
        return awsProperties;
    }

    @Bean
    public S3Service s3Service(AmazonS3 s3Client, AwsProperties awsProperties) {
        return new S3ServiceImpl(s3Client, awsProperties);
    }

    @Bean
    public TagRepository tagRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativeTagRepository(jdbcTemplate);
    }

    @Bean
    public ImageRepository imageRepository(JdbcTemplate jdbcTemplate, S3Service s3Service) {
        return new JdbcNativeImageRepository(jdbcTemplate, s3Service);
    }

    @Bean
    public CommentRepository commentRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcNativeCommentRepository(jdbcTemplate);
    }

    @Bean
    public PostRepository postRepository(
            JdbcTemplate jdbcTemplate,
            TagRepository tagRepository,
            ImageRepository imageRepository,
            CommentRepository commentRepository
    ) {
        return new JdbcNativePostRepository(jdbcTemplate, tagRepository, imageRepository, commentRepository);
    }
}