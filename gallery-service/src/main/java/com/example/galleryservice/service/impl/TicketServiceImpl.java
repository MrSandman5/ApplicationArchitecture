package com.example.galleryservice.service.impl;

import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.project.Ticket;
import com.example.galleryservice.model.user.User;
import com.example.galleryservice.repository.ClientRepository;
import com.example.galleryservice.repository.TicketRepository;
import com.example.galleryservice.repository.UserRepository;
import com.example.galleryservice.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public TicketServiceImpl(@NotNull final TicketRepository ticketRepository,
                             @NotNull final ClientRepository clientRepository) {
        this.ticketRepository = ticketRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Ticket> getAllTickets() {
        List<Ticket> result = ticketRepository.findAll();
        log.info("IN getAllTickets<Ticket> - {} tickets found", result.size());
        return result;
    }

    @Override
    public List<Ticket> findTicketsByClient(@NotNull User user) {
        List<Ticket> result = ticketRepository.findTicketsByClient(user);
        log.info("IN findTicketsByClient<Ticket> - {} tickets found", result.size());
        return result;
    }

    @Override
    public List<Ticket> findTicketsByExpo(@NotNull Expo expo) {
        List<Ticket> result = ticketRepository.findTicketsByExpo(expo);
        log.info("IN findTicketsByExpo<Ticket> - {} tickets found", result.size());
        return result;
    }

    @Override
    public Ticket findTicketById(Long id) {
        Ticket result = ticketRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findTicketById<Ticket> - no ticket found by id: {}", id);
            return null;
        }

        log.info("IN findTicketById<Ticket> - ticket: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
        log.info("IN deleteTicket<Ticket> - ticket with id: {} successfully deleted", id);
    }

    @Override
    public void deleteTicketsByUser(@NotNull User user) {
        ticketRepository.deleteTicketsByClient(user);
        log.info("IN deleteTicketsByUser<Ticket> - tickets with user: {} successfully deleted", user);
    }

    @Override
    public void deleteTicketsByExpo(@NotNull Expo expo) {
        ticketRepository.deleteTicketsByExpo(expo);
        log.info("IN deleteTicketsByExpo<Ticket> - tickets with expo: {} successfully deleted", expo);
    }
}
