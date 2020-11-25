package com.example.galleryservice.controller;

import com.example.galleryservice.service.impl.GalleryFacadeImpl;
import com.example.galleryservice.view.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@Data
public class OwnerController {

    private final GalleryFacadeImpl facade;

    @Autowired
    public OwnerController(@NotNull final GalleryFacadeImpl facade){
        this.facade = facade;
    }

    @GetMapping("/owner/{login}")
    public String ownerPage(@NotNull final Model model,
                           @PathVariable @NotNull final String login){
        model.addAttribute("user", facade.getOwner(login));
        model.addAttribute("expos", facade.getOwnerExpos(login));
        return "owner";
    }

    @GetMapping("/owner/{login}/createExpo")
    public String createExpo(@NotNull final Model model,
                            @PathVariable @NotNull final String login){
        model.addAttribute("expo", new ExpoForm());
        model.addAttribute("login", login);
        return "createExpo";
    }

    @PostMapping("/owner/{login}/createExpo")
    public String createExpo(@Valid @ModelAttribute @NotNull final ExpoForm expoForm,
                             @PathVariable @NotNull final String login){
        facade.createExpo(login,
                expoForm.getName(),
                expoForm.getInfo(),
                expoForm.getArtist(),
                expoForm.getStartTime(),
                expoForm.getEndTime(),
                expoForm.getTicketPrice(),
                facade.getArtist(expoForm.getArtist()).getArtworks());
        return "redirect:/owner/" + login;
    }

    @GetMapping("/owner/{login}/editExpo")
    public String editExpo(@NotNull final Model model,
                             @PathVariable @NotNull final String login){
        model.addAttribute("settings", new EditExpoForm());
        model.addAttribute("login", login);
        return "editExpo";
    }

    @PostMapping("/owner/{login}/editExpo")
    public String editExpo(@Valid @ModelAttribute @NotNull final EditExpoForm editExpoForm,
                             @PathVariable @NotNull final String login){
        facade.editExpo(login, editExpoForm.getExpo(), editExpoForm.getSetting(), editExpoForm.getData());
        return "redirect:/owner/" + login;
    }

    @GetMapping("/owner/{login}/openExpo")
    public String openExpo(@NotNull final Model model,
                           @PathVariable @NotNull final String login){
        model.addAttribute("expo", new OpenCloseExpoForm());
        model.addAttribute("login", login);
        return "openExpo";
    }

    @PostMapping("/owner/{login}/openExpo")
    public String openExpo(@Valid @ModelAttribute @NotNull final OpenCloseExpoForm openCloseExpoForm,
                           @PathVariable @NotNull final String login){
        facade.startExpo(login, openCloseExpoForm.getExpo());
        return "redirect:/owner/" + login;
    }

    @GetMapping("/owner/{login}/closeExpo")
    public String closeExpo(@NotNull final Model model,
                           @PathVariable @NotNull final String login){
        model.addAttribute("expo", new OpenCloseExpoForm());
        model.addAttribute("login", login);
        return "closeExpo";
    }

    @PostMapping("/owner/{login}/closeExpo")
    public String closeExpo(@Valid @ModelAttribute @NotNull final OpenCloseExpoForm openCloseExpoForm,
                           @PathVariable @NotNull final String login){
        facade.closeExpo(login, openCloseExpoForm.getExpo());
        return "redirect:/owner/" + login;
    }

    @GetMapping("/owner/{login}/payForExpo")
    public String payForExpo(@NotNull final Model model,
                            @PathVariable @NotNull final String login){
        model.addAttribute("expo", new PaymentExpoForm());
        model.addAttribute("login", login);
        return "payForExpo";
    }

    @PostMapping("/owner/{login}/payForExpo")
    public String payForExpo(@Valid @ModelAttribute @NotNull final PaymentExpoForm paymentExpoForm,
                            @PathVariable @NotNull final String login){
        facade.payForExpo(login, paymentExpoForm.getExpo());
        return "redirect:/owner/" + login;
    }

}
