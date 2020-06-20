package com.example.galleryservice.controller;

import com.example.galleryservice.service.impl.GalleryFacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Controller
public class UserController {

    private final GalleryFacadeImpl facade;

    @Autowired
    public UserController(@NotNull final GalleryFacadeImpl facade){
        this.facade = facade;
    }

    @GetMapping("/users")
    public String all(@NotNull final Model model) {
        model.addAttribute("users", facade.getAllUsers());
        return "users";
    }
}
