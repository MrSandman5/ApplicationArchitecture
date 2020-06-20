package com.example.galleryservice.controller;

import com.example.galleryservice.model.user.Artist;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import com.example.galleryservice.service.impl.GalleryFacadeImpl;
import com.example.galleryservice.view.SignInForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
public class SignInController {

    private final GalleryFacadeImpl facade;

    @Autowired
    public SignInController(@NotNull final GalleryFacadeImpl facade){
        this.facade = facade;
    }

    @GetMapping("/signIn")
    public String signIn(@NotNull final Model model){
        model.addAttribute("user", new SignInForm());
        return "signIn";
    }

    @PostMapping("/signIn")
    public String signIn(@Valid @ModelAttribute @NotNull final SignInForm signInForm) {
        facade.authenticate(signInForm.getLogin(), signInForm.getPassword());
        String path = null;
        final Client client = facade.getClient(signInForm.getLogin());
        if (client != null){
            path = "redirect:/client/" + signInForm.getLogin();
            return path;
        }
        final Owner owner = facade.getOwner(signInForm.getLogin());
        if (owner != null){
            path = "redirect:/owner/" + signInForm.getLogin();
            return path;
        }
        final Artist artist = facade.getArtist(signInForm.getLogin());
        if (artist != null){
            path = "redirect:/artist/" + signInForm.getLogin();
            return path;
        }
        return path;
    }
}
