// [경로] src/main/java/com/example/movie_manager/config/WebConfig.java
package com.example.movie_manager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dir = StringUtils.trimTrailingCharacter(uploadDir.replace("\\", "/"), '/');
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///" + dir + "/");
    }
}
