package com.study.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/upload/review/**")
                .addResourceLocations("file:///C:/upload/review/");

        registry.addResourceHandler("/upload/notice/**")
                .addResourceLocations("file:///C:/upload/notice/");
    }
}





