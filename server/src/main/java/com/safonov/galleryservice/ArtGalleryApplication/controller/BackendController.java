package com.safonov.galleryservice.ArtGalleryApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BackendController {

    @GetMapping("/greeting")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
