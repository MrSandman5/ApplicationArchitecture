package com.example.galleryservice.controller;

import com.example.galleryservice.service.impl.GalleryFacadeImpl;
import com.example.galleryservice.view.PaymentReservationForm;
import com.example.galleryservice.view.TicketForm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@Data
public class ClientController {

    private final GalleryFacadeImpl facade;

    @Autowired
    public ClientController(@NotNull final GalleryFacadeImpl facade){
        this.facade = facade;
    }

    @GetMapping("/client/{login}")
    public String clientPage(@NotNull final Model model,
                           @PathVariable @NotNull final String login){
        model.addAttribute("user", facade.getClient(login));
        model.addAttribute("tickets", facade.getClientTickets(login));
        model.addAttribute("reservations", facade.getClientReservations(login));
        return "client";
    }

    @GetMapping("/client/{login}/addTicket")
    public String addTicket(@NotNull final Model model,
                            @PathVariable @NotNull final String login){
        model.addAttribute("ticket", new TicketForm());
        model.addAttribute("login", login);
        return "addTicket";
    }

    @PostMapping("/client/{login}/addTicket")
    public String addTicket(@Valid @ModelAttribute @NotNull final TicketForm ticketForm,
                            @PathVariable @NotNull final String login) {
        facade.addTicket(login, ticketForm.getExpo());
        return "redirect:/client/" + login;
    }

    @GetMapping("/client/{login}/createRes")
    public String createReservation(@PathVariable @NotNull final String login){
        facade.createReservation(login);
        return "redirect:/client/" + login;
    }

    @GetMapping("/client/{login}/payRes")
    public String payForReservation(@NotNull final Model model,
                                    @PathVariable @NotNull final String login){
        model.addAttribute("payment", new PaymentReservationForm());
        model.addAttribute("login", login);
        return "payRes";
    }

    @PostMapping("/client/{login}/payRes")
    public String payForReservation(@Valid @ModelAttribute @NotNull final PaymentReservationForm paymentReservationForm,
                                    @PathVariable @NotNull final String login){
        facade.payForReservation(login, paymentReservationForm.getReservation(), paymentReservationForm.getOwner());
        return "redirect:/client/" + login;
    }

}
