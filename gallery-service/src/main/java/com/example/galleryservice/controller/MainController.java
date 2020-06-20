package com.example.galleryservice.controller;

import com.example.galleryservice.service.impl.GalleryFacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class MainController {

    private final GalleryFacadeImpl facade;

    @Autowired
    public MainController(@NotNull final GalleryFacadeImpl facade){
        this.facade = facade;
    }

}
