package ru.yandex.practicum.config;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@Import({DataSourceConfig.class, ThymeleafConfig.class, MultipartFileConfig.class, S3Config.class})
@ComponentScan(basePackages = "ru.yandex.practicum")
public class WebConfig {
}