package com.safonov.galleryservice.ArtGalleryApplication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackendController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
