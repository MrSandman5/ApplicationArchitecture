package com.example.galleryservice;

import com.example.galleryservice.configuration.DataSourceConfig;
import com.example.galleryservice.data.StorageDAO;
import com.example.galleryservice.model.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan({"com.example.galleryservice.configuration", "com.example.galleryservice.data"})
@EnableJdbcRepositories(value = {"com.example.galleryservice.data.project", "com.example.galleryservice.data.user"})
public class GalleryServiceApplication {

    public static void main(String[] args) {
        //SpringApplication.run(GalleryServiceApplication.class, args);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);

        StorageDAO storageDAO = context.getBean(StorageDAO.class);

        System.out.println("List of person is:");

        for (User p : storageDAO.getUserDAO().findAll()) {
            System.out.println(p);
        }
    }

}
