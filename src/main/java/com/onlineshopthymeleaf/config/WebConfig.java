package com.onlineshopthymeleaf.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.client-path}")
    protected String fileClientPath;

    @Value("${server.path}")
    protected String serverPath;

    private Path root;

    @PostConstruct
    public void init() {
        this.root = Paths.get(serverPath);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resolvedPath = "file:" + serverPath;
        registry.addResourceHandler(fileClientPath)
                .addResourceLocations(resolvedPath);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }
    public Path getRootPath() {
        return root;
    }

}
