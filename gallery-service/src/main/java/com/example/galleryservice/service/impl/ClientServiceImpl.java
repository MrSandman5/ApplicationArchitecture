package com.example.galleryservice.service.impl;

import com.example.galleryservice.exceptions.IncorrectPasswordException;
import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.project.Reservation;
import com.example.galleryservice.model.project.ReservationStatus;
import com.example.galleryservice.model.project.Ticket;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.repository.*;
import com.example.galleryservice.service.ClientService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final ExpoRepository expoRepository;

    @Autowired
    public ClientServiceImpl(@NotNull final ClientRepository clientRepository,
                             @NotNull final ReservationRepository reservationRepository,
                             @NotNull final TicketRepository ticketRepository,
                             @NotNull final ExpoRepository expoRepository) {
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
        this.ticketRepository = ticketRepository;
        this.expoRepository = expoRepository;
    }

    @Override
    public void register(@NotNull final String login,
                         @NotNull final String password,
                         @NotNull final String name,
                         @NotNull final String email) {
        final Client registeredClient = clientRepository.save(
                new Client(login, password, name, email));
        log.info("IN register<Client> - client: {} successfully registered", registeredClient);
    }

    @Override
    public void register(@NotNull final Client user) {
        final Client registeredClient = clientRepository.save(user);
        log.info("IN register<Client> - client: {} successfully registered", registeredClient);
    }

    @SneakyThrows
    @Override
    public void signIn(@NotNull final String login, @NotNull final String password) {
        final Client client = this.findUserByLogin(login);
        if (client.isAuthenticated()){
            return;
        }
        if (!password.equals(client.getPassword())){
            throw new IncorrectPasswordException();
        }
        client.setAuthentication(true);
        log.info("IN signIn<Client> - client: {} successful signIn", client);
    }

    @Override
    public void signOut(@NotNull final String login) {
        final Client client = this.findUserByLogin(login);
        if (!client.isAuthenticated()){
            return;
        }
        client.setAuthentication(false);
        log.info("IN signOut<Client> - client: {} successful signOut", client);
    }

    @Override
    public List<Client> getAllUsers() {
        final List<Client> result = clientRepository.findAll();
        log.info("IN getAllUsers<Client> - {} clients found", result.size());
        return result;
    }

    @Override
    public Client findUserByLogin(@NotNull final String login) {
        final Client result = clientRepository.findUserByLogin(login);
        log.info("IN findUserByLogin<Client> - client: found by login: {}", login);
        return result;
    }

    @Override
    public Client findUserByEmail(@NotNull final String email) {
        final Client result = clientRepository.findUserByEmail(email);
        log.info("IN findUserByEmail<Client> - client: found by email: {}", email);
        return result;
    }

    @Override
    public Client findUserById(final Long id) {
        final Client result = clientRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findUserById<Client> - no client found by id: {}", id);
            return null;
        }

        log.info("IN findUserById<Client> - client: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteUser(final Long id) {
        clientRepository.deleteById(id);
        log.info("IN deleteUser<Client> - client with id: {} successfully deleted", id);
    }

    @SneakyThrows
    public void addTicket(@NotNull final Client client,
                                       @NotNull final Ticket ticket){
        client.checkAuthentication();
        final Expo ticketExpo = expoRepository.findById(ticket.getExpo().getId()).orElse(null);
        if (ticketExpo == null) {
            log.warn("IN addTicket - no expo found for ticket: {}", ticket);
            return;
        } else if (ticketExpo.isClosed()){
            log.warn("IN addTicket - expo: {} already closed", ticketExpo);
            return;
        }
        final Ticket addedTicket = ticketRepository.save(ticket);
        log.info("IN addTicket - ticket: {} successfully added to list", addedTicket);
        client.getTickets().add(ticket);
    }

    @SneakyThrows
    public Reservation createReservation(@NotNull final Client client){
        client.checkAuthentication();
        final Expo ticketExpo = expoRepository.findById(client.getTickets().get(0).getExpo().getId()).orElse(null);
        if (ticketExpo == null) {
            log.warn("IN createReservation - no expo found");
            return null;
        } else if (ticketExpo.isClosed()){
            log.warn("IN addTicket - expo: {} already closed", ticketExpo);
            return null;
        }
        final Reservation reservation = new Reservation(client, client.getTickets(), ticketExpo.getStartTime());
        final Reservation addedReservation = reservationRepository.save(reservation);
        log.info("IN createReservation - reservation: {} successfully added to list", addedReservation);
        client.getReservations().add(addedReservation);
        return addedReservation;
    }

    @SneakyThrows
    public Double payForReservation(@NotNull final Client client,
                                    @NotNull final Reservation reservation){
        client.checkAuthentication();
        final Reservation clientReservation = reservationRepository.findById(reservation.getId()).orElse(null);
        if (clientReservation == null){
            log.warn("IN payForReservation - no reservation found");
            return null;
        } else if (clientReservation.isClosed()){
            log.warn("IN payForReservation - reservation : {} was already closed", client);
            return null;
        } else if (client.getReservations().contains(clientReservation)){
            log.warn("IN payForReservation - no reservation found for client {}", client);
            return null;
        }
        final Expo ticketExpo = expoRepository.findById(clientReservation.getTickets().get(0).getExpo().getId()).orElse(null);
        if (ticketExpo == null) {
            log.warn("IN payForReservation - no expo found for reservation {}", reservation);
            return null;
        } else if (ticketExpo.isStarted()
                || LocalDateTime.now().until(ticketExpo.getStartTime(), ChronoUnit.MINUTES) < 30) {
            return buyTickets(client, clientReservation);
        } else if (ticketExpo.isClosed()){
            log.warn("IN payForReservation - expo: {} already closed", ticketExpo);
            return null;
        }
        final Double payment = clientReservation.getTickets().stream()
                .map(Ticket::getCost)
                .mapToDouble(Integer::doubleValue)
                .sum();

        clientReservation.setStatus(ReservationStatus.PAYED);
        log.warn("IN payForReservation - payment: {} was payed for reservation {}", payment, clientReservation);
        return payment;
    }

    private Double buyTickets(@NotNull final Client client,
                              @NotNull final Reservation reservation){
        client.getTickets().addAll(reservation.getTickets());
        reservationRepository.delete(reservation);
        final Double payment = client.getTickets().stream()
                .map(Ticket::getCost)
                .mapToDouble(Integer::doubleValue)
                .sum();

        log.warn("IN buyTickets - payment: {} was payed for tickets", payment);
        return payment;
    }
}
