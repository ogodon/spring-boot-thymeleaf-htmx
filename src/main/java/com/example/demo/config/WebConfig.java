package com.example.demo.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC configuration for static resource management. This class configures handlers for
 * static resources (CSS, JS, images) with a 5-minute cache policy.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final int CACHE_DURATION_MINUTES = 5;

    /**
     * Configures handlers for static resources. Defines paths and cache policy for each resource
     * type. Cache is set to 5 minutes duration.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure static resources with 5-minute cache
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
                .setCacheControl(CacheControl.maxAge(CACHE_DURATION_MINUTES, TimeUnit.MINUTES));

        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
                .setCacheControl(CacheControl.maxAge(CACHE_DURATION_MINUTES, TimeUnit.MINUTES));

        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(CACHE_DURATION_MINUTES, TimeUnit.MINUTES));
    }
}
