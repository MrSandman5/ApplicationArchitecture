package com.example.galleryservice.service.impl;

import com.example.galleryservice.exceptions.IncorrectPasswordException;
import com.example.galleryservice.model.project.Expo;
import com.example.galleryservice.model.project.ExpoStatus;
import com.example.galleryservice.model.project.Reservation;
import com.example.galleryservice.model.project.ReservationStatus;
import com.example.galleryservice.model.user.Artist;
import com.example.galleryservice.model.user.Client;
import com.example.galleryservice.model.user.Owner;
import com.example.galleryservice.repository.*;
import com.example.galleryservice.service.OwnerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final ExpoRepository expoRepository;
    private final ReservationRepository reservationRepository;
    private final ClientServiceImpl clientService;
    private final ArtistServiceImpl artistService;

    @Autowired
    public OwnerServiceImpl(@NotNull final OwnerRepository ownerRepository,
                            @NotNull final ExpoRepository expoRepository,
                            @NotNull final ReservationRepository reservationRepository,
                            @NotNull final ClientServiceImpl clientService,
                            @NotNull final ArtistServiceImpl artistService) {
        this.ownerRepository = ownerRepository;
        this.expoRepository = expoRepository;
        this.reservationRepository = reservationRepository;
        this.clientService = clientService;
        this.artistService = artistService;
    }

    @Override
    public void register(@NotNull final String login,
                         @NotNull final String password,
                         @NotNull final String name,
                         @NotNull final String email) {
        Owner registeredOwner = ownerRepository.save(
                new Owner(login, password, name, email));
        log.info("IN register<Owner> - owner: {} successfully registered", registeredOwner);
    }

    @Override
    public void register(@NotNull final Owner user) {
        Owner registeredOwner = ownerRepository.save(user);
        log.info("IN register<Owner> - owner: {} successfully registered", registeredOwner);
    }

    @SneakyThrows
    @Override
    public void signIn(@NotNull final String login, @NotNull final String password) {
        Owner owner = this.findUserByLogin(login);
        if (owner.isAuthenticated()){
            return;
        }
        if (!password.equals(owner.getPassword())){
            throw new IncorrectPasswordException();
        }
        owner.setAuthentication(true);
        log.info("IN authenticate - owner: {} successful signIn", owner);
    }

    @Override
    public void signOut(@NotNull final String login) {
        Owner owner = this.findUserByLogin(login);
        if (!owner.isAuthenticated()){
            return;
        }
        owner.setAuthentication(false);
        log.info("IN authenticate - owner: {} successful signOut", owner);
    }

    @Override
    public List<Owner> getAllUsers() {
        List<Owner> result = ownerRepository.findAll();
        log.info("IN getAllUsers<Owner> - {} owners found", result.size());
        return result;
    }

    @Override
    public Owner findUserByLogin(@NotNull final String login) {
        Owner result = ownerRepository.findUserByLogin(login);
        log.info("IN findUserByLogin<Owner> - owner: found by login: {}", login);
        return result;
    }

    @Override
    public Owner findUserByEmail(@NotNull final String email) {
        Owner result = ownerRepository.findUserByEmail(email);
        log.info("IN findUserByEmail<Owner> - owner: found by email: {}", email);
        return result;
    }

    @Override
    public Owner findUserById(final Long id) {
        Owner result = ownerRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findUserById<Owner> - no owner found by id: {}", id);
            return null;
        }

        log.info("IN findUserById<Owner> - owner: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteUser(final Long id) {
        ownerRepository.deleteById(id);
        log.info("IN deleteUser<Owner> - owner with id: {} successfully deleted", id);
    }

    @SneakyThrows
    public Double acceptPayment(@NotNull final Owner owner,
                              @NotNull final Client client,
                              @NotNull final Reservation reservation){
        owner.checkAuthentication();
        final Double payment = clientService.payForReservation(client, reservation);
        if (!reservation.isPayed()){
            log.warn("IN acceptPayment - reservation : {} wasn't payed", reservation);
            return null;
        }
        reservation.setStatus(ReservationStatus.CLOSED);
        client.getTickets().addAll(reservation.getTickets());
        log.warn("IN payForReservation - payment: {} for reservation {} was accepted", payment, reservation);
        return payment;
    }

    @SneakyThrows
    public Expo createExpo(@NotNull final Owner owner,
                           @NotNull final Expo expo){
        owner.checkAuthentication();
        Expo newExpo = expoRepository.findById(expo.getId()).orElse(null);
        if (newExpo != null){
            log.warn("IN createExpo - expo: {} already existed", expo);
            return null;
        }
        newExpo = expoRepository.save(expo);
        log.info("IN createExpo - expo : {} successfully created", expo);
        return newExpo;
    }

    @SneakyThrows
    public void editExpo(@NotNull final Owner owner){
        owner.checkAuthentication();
    }

    @SneakyThrows
    public void startExpo(@NotNull final Owner owner){
        owner.checkAuthentication();
        final List<Expo> newExpos = expoRepository.findExposByStatus(ExpoStatus.NEW);
        for (Expo expo : newExpos) {
            if (expo.getStartTime().isAfter(LocalDateTime.now())
                    && LocalDateTime.now().isBefore(expo.getEndTime())){
                expo.setStatus(ExpoStatus.STARTED);
            }
        }
    }

    @SneakyThrows
    public void closeExpo(@NotNull final Owner owner){
        owner.checkAuthentication();
        final List<Expo> startedExpos = expoRepository.findExposByStatus(ExpoStatus.STARTED);
        for (Expo expo : startedExpos) {
            if (LocalDateTime.now().isAfter(expo.getEndTime())){
                expo.setStatus(ExpoStatus.CLOSED);
            }
        }
    }

    @SneakyThrows
    public Double payForExpo(@NotNull final Owner owner,
                           @NotNull final Expo expo){
        owner.checkAuthentication();
        return null;
    }

}
