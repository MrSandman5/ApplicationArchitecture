package com.safonov.galleryservice.ArtGalleryApplication.model.user;

import com.safonov.galleryservice.ArtGalleryApplication.exceptions.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class Owner extends User {

    @OneToMany(mappedBy = "expo", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<Expo> expos = new ArrayList<>();

    @OneToMany(mappedBy = "client_owner_payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<ClientOwnerPayment> clientOwnerPayments = new ArrayList<>();

    @OneToMany(mappedBy = "owner_artist_payment", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private final List<OwnerArtistPayment> ownerArtistPayments = new ArrayList<>();

    public Owner(@NotNull final String login,
                 @NotNull final String password,
                 @NotNull final String name,
                 @NotNull final String email) {
        super(login, password, name, email);
    }

    public Owner(@NotNull final User user) {
        super(user);
    }

    @SneakyThrows
    public double acceptPayment(final long reservation) {
        this.checkAuthentication();
        final Reservation payedReservation = getStorageRepository().getReservation(reservation);
        if (payedReservation == null) {
            throw new ReservationNotFoundException(reservation);
        } else if (!payedReservation.isPayed()) {
            throw new ReservationNotPaidException(payedReservation.getId());
        }
        final ClientOwnerPayment payment = getStorageRepository().getClientOwnerPaymentByReservation(payedReservation.getId());
        payedReservation.setStatus(ReservationStatus.Closed);
        getStorageRepository().updateReservation(payedReservation);
        return payment.getPrice();
    }

    @SneakyThrows
    public Expo createExpo(@NotNull final String name,
                           @NotNull final String info,
                           @NotNull final Artist artist,
                           @NotNull final LocalDateTime startTime,
                           @NotNull final LocalDateTime endTime,
                           final double ticketPrice,
                           @NotNull final List<Artwork> artworks) {
        this.checkAuthentication();
        final Expo existedExpo = getStorageRepository().getExpo(name);
        if (existedExpo == null) {
            final Expo newExpo = new Expo(name, info, artist, startTime, endTime, ticketPrice, artworks);
            final long expoId = getStorageRepository().addExpo(newExpo).getId();
            final Expo addedExpo = getStorageRepository().getExpo(expoId);
            this.expos.add(addedExpo);
            return addedExpo;
        }
        if (expos.contains(existedExpo))
            throw new ExpoAlreadyExistedException("Expo with id : " + existedExpo.getId() + " already existed for this user!");
        this.expos.add(existedExpo);
        getStorageRepository().updateOwner(this);
        return existedExpo;
    }

    @SneakyThrows
    public Expo createExpo(@NotNull final Expo expo) {
        this.checkAuthentication();
        if (expos.contains(expo))
            throw new ExpoAlreadyExistedException("Expo with id : " + expo.getId() + " already existed for this user!");
        final Expo existedExpo = getStorageRepository().getExpo(expo.getName());
        if (existedExpo == null) {
            final long expoId = getStorageRepository().addExpo(expo).getId();
            final Expo addedExpo = getStorageRepository().getExpo(expoId);
            this.expos.add(addedExpo);
            return addedExpo;
        }
        this.expos.add(existedExpo);
        getStorageRepository().updateOwner(this);
        return existedExpo;
    }

    @SneakyThrows
    public void editExpo(@NotNull final String expo,
                         @NotNull final EditSettings settings,
                         @NotNull final String data) {
        this.checkAuthentication();
        final Expo curExpo = getStorageRepository().getExpo(expo);
        if (curExpo == null) {
            throw new ExpoNotFoundException(expo);
        }
        final LocalDateTime newDateTime;
        switch (settings) {
            case Name:
                curExpo.setName(data);
                break;
            case Info:
                curExpo.setInfo(data);
                break;
            case StartTime:
                newDateTime = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (newDateTime.isBefore(curExpo.getStartTime()) && LocalDateTime.now().isBefore(newDateTime)
                        || newDateTime.isAfter(curExpo.getStartTime()) && newDateTime.isBefore(curExpo.getEndTime()))
                    curExpo.setStartTime(newDateTime);
                break;
            case EndTime:
                newDateTime = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (newDateTime.isBefore(curExpo.getEndTime()) && newDateTime.isAfter(curExpo.getStartTime())
                        || newDateTime.isAfter(curExpo.getEndTime()))
                    curExpo.setEndTime(newDateTime);
                break;
            default:
                break;
        }
        getStorageRepository().updateExpo(curExpo);
    }

    @SneakyThrows
    public void startExpo(@NotNull final String expo) {
        this.checkAuthentication();
        final Expo newExpo = getStorageRepository().getExpo(expo);
        if (newExpo == null) {
            throw new ExpoNotFoundException(expo);
        } else if ((LocalDateTime.now().isAfter(newExpo.getStartTime()))
                && LocalDateTime.now().isBefore(newExpo.getEndTime())) {
            newExpo.setStatus(ExpoStatus.Opened);
            getStorageRepository().updateExpo(newExpo);
        }
    }

    @SneakyThrows
    public void closeExpo(@NotNull final String expo) {
        this.checkAuthentication();
        final Expo openedExpo = getStorageRepository().getExpo(expo);
        if (openedExpo == null) {
            throw new ExpoNotFoundException(expo);
        } else if (!openedExpo.isOpened()) {
            throw new ExpoHasNotOpenedException("Expo with id : " + openedExpo.getId() + " hasn't started!");
        } else if (LocalDateTime.now().isAfter(openedExpo.getStartTime())
                && LocalDateTime.now().isBefore(openedExpo.getEndTime())
                || LocalDateTime.now().isAfter(openedExpo.getEndTime())) {
            openedExpo.setStatus(ExpoStatus.Closed);
            getStorageRepository().updateExpo(openedExpo);
        }
    }

    @SneakyThrows
    public double payForExpo(@NotNull final String expo) {
        this.checkAuthentication();
        final Expo closedExpo = getStorageRepository().getExpo(expo);
        if (closedExpo == null) {
            throw new ExpoNotFoundException(expo);
        } else if (!closedExpo.isClosed()) {
            throw new ExpoHasNotClosedException("Expo with id : " + closedExpo.getId() + " hasn't closed!");
        }
        final List<Reservation> expoReservations = getStorageRepository().getReservationsByStatus(ReservationStatus.Closed)
                .stream().filter(reservation ->
                        getStorageRepository().getTicket(reservation.getTickets().get(0).getId()).getExpo().getId().equals(closedExpo.getId()))
                .collect(Collectors.toList());
        double payment = 0;
        for (Reservation entry : expoReservations) {
            payment += entry.getCost();
        }
        final OwnerArtistPayment expoPayment = new OwnerArtistPayment(closedExpo, this, closedExpo.getArtist(), payment * 0.5);
        final long ownerArtistPaymentId = getStorageRepository().addOwnerArtistPayment(expoPayment);
        this.expos.remove(closedExpo);
        getStorageRepository().updateOwner(this);
        return getStorageRepository().getPayment(ownerArtistPaymentId).getPrice();
    }
}
