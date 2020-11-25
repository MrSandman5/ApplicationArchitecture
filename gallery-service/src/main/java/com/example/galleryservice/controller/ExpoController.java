package com.example.galleryservice.controller;

import com.example.galleryservice.service.impl.GalleryFacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.constraints.NotNull;

@Controller
public class ExpoController {

    private final GalleryFacadeImpl facade;

    @Autowired
    public ExpoController(@NotNull final GalleryFacadeImpl facade){
        this.facade = facade;
    }

    @GetMapping("/expos")
    public String all(@NotNull final Model model) {
        model.addAttribute("expos", facade.getAllExpos());
        return "expos";
    }
}
