package com.example.galleryservice;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configurable
@SpringBootApplication
@EnableJdbcRepositories(value = {"com.example.galleryservice.data.project", "com.example.galleryservice.data.user"})
public class GalleryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalleryServiceApplication.class, args);
    }

}
