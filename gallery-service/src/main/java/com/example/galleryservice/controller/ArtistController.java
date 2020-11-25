package com.example.galleryservice.controller;

import com.example.galleryservice.service.impl.GalleryFacadeImpl;
import com.example.galleryservice.view.ArtworkForm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@Data
public class ArtistController {

    private final GalleryFacadeImpl facade;

    @Autowired
    public ArtistController(@NotNull final GalleryFacadeImpl facade){
        this.facade = facade;
    }

    @GetMapping("/artist/{login}")
    public String artistPage(@NotNull final Model model,
                            @PathVariable @NotNull final String login){
        model.addAttribute("user", facade.getArtist(login));
        model.addAttribute("artworks", facade.getArtistArtworks(login));
        return "artist";
    }

    @GetMapping("/artist/{login}/addArtwork")
    public String addArtwork(@NotNull final Model model,
                             @PathVariable @NotNull final String login){
        model.addAttribute("login", login);
        model.addAttribute("artwork", new ArtworkForm());
        return "addArtwork";
    }

    @PostMapping("/artist/{login}/addArtwork")
    public String addArtwork(@Valid @ModelAttribute @NotNull final ArtworkForm artworkForm,
                            @PathVariable @NotNull final String login) {
        facade.addArtwork(login, artworkForm.getName(), artworkForm.getInfo());
        return "redirect:/artist/" + login;
    }


}
