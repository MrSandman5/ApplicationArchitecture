package com.example.galleryservice.model.user;

import com.example.galleryservice.exceptions.*;
import com.example.galleryservice.model.gallery.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Owner extends User {

    private final List<Expo> expos = new ArrayList<>();

    public Owner(@NotNull final String login,
                 @NotNull final String password,
                 @NotNull final String name,
                 @NotNull final String email) {
        super(login, password, name, email, UserRole.Owner);
    }

    public Owner(@NotNull final User user) {
        super(user);
    }

    @SneakyThrows
    public double acceptPayment(@NotNull final Reservation reservation){
        this.checkAuthentication();
        final Reservation payedReservation = getStorageDAO().getReservation(reservation.getId());
        if (payedReservation == null){
            throw new ReservationNotFoundException(reservation.getId());
        } else if (!payedReservation.isPayed()){
            throw new ReservationNotPaidException(payedReservation.getId());
        }
        final ClientOwnerPayment payment = getStorageDAO().getClientOwnerPayment(payedReservation.getId());
        payedReservation.setStatus(ReservationStatus.Closed);
        getStorageDAO().updateReservation(payedReservation);
        return payment.getAmount();
    }

    @SneakyThrows
    public Expo createExpo(@NotNull final Expo expo){
        this.checkAuthentication();
        if (expos.contains(expo))
            throw new ExpoAlreadyExistedException("Artwork with id : " + expo.getId() + " already existed for this user!");
        final Expo existedExpo = getStorageDAO().getExpo(expo.getName());
        if (existedExpo == null) {
            final long expoId = getStorageDAO().addExpo(expo);
            final Expo addedExpo = getStorageDAO().getExpo(expoId);
            this.expos.add(addedExpo);
            return addedExpo;
        }
        this.expos.add(existedExpo);
        return existedExpo;
    }

    @SneakyThrows
    public void editExpo(@NotNull final Expo expo,
                         @NotNull final EditSettings settings,
                         @NotNull final String data){
        this.checkAuthentication();
        final Expo curExpo = getStorageDAO().getExpo(expo.getName());
        final LocalDateTime newDateTime;
        switch (settings){
            case Name:
                curExpo.setName(data);
                break;
            case Info:
                curExpo.setInfo(data);
                break;
            case StartTime:
                newDateTime = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (newDateTime.isBefore(expo.getStartTime()) && LocalDateTime.now().isBefore(newDateTime)
                        || newDateTime.isAfter(expo.getStartTime()) && newDateTime.isBefore(expo.getEndTime()))
                    curExpo.setStartTime(newDateTime);
                break;
            case EndTime:
                newDateTime = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (newDateTime.isBefore(expo.getEndTime()) && newDateTime.isAfter(expo.getStartTime())
                        || newDateTime.isAfter(expo.getEndTime()))
                    curExpo.setEndTime(newDateTime);
                break;
            default:
                break;
        }
        getStorageDAO().updateExpo(expo);
    }

    @SneakyThrows
    public void startExpo(@NotNull final Expo expo){
        this.checkAuthentication();
        final Expo newExpo = getStorageDAO().getExpo(expo.getName());
        if (newExpo == null){
            throw new ExpoNotFoundException(expo.getId());
        } else if ((LocalDateTime.now().equals(newExpo.getStartTime())
                || LocalDateTime.now().isAfter(newExpo.getStartTime()))
                && LocalDateTime.now().isBefore(newExpo.getEndTime())) {
            newExpo.setStatus(ExpoStatus.Opened);
            getStorageDAO().updateExpo(newExpo);
        }
    }

    @SneakyThrows
    public void closeExpo(@NotNull final Expo expo){
        this.checkAuthentication();
        final Expo openedExpo = getStorageDAO().getExpo(expo.getName());
        if (openedExpo == null){
            throw new ExpoNotFoundException(expo.getId());
        } else if (!openedExpo.isOpened()) {
            throw new ExpoHasNotOpenedException("Expo with id : " + openedExpo.getId() + " hasn't started!");
        } else if (LocalDateTime.now().equals(openedExpo.getEndTime())
                || LocalDateTime.now().isAfter(openedExpo.getEndTime())) {
            openedExpo.setStatus(ExpoStatus.Closed);
            getStorageDAO().updateExpo(openedExpo);
        }
    }

    @SneakyThrows
    public double payForExpo(@NotNull final Expo expo){
        this.checkAuthentication();
        final Expo closedExpo = getStorageDAO().getExpo(getName());
        if (closedExpo == null) {
            throw new ExpoNotFoundException(expo.getId());
        } else if (!closedExpo.isOpened()) {
            throw new ExpoHasNotClosedException("Expo with id : " + closedExpo.getId() + " hasn't started!");
        }
        final List<Reservation> expoReservations = getStorageDAO().getReservationsByStatus(ReservationStatus.Closed.toString())
                .stream().filter(reservation -> reservation.getTickets().get(0).getExpo() == closedExpo.getId())
                .collect(Collectors.toList());
        double payment = 0;
        for (Reservation entry : expoReservations){
            payment += entry.getCost();
        }

        final OwnerArtistPayment expoPayment = new OwnerArtistPayment(closedExpo.getId(), this.getId(), expo.getArtist(), payment * 0.5);
        getStorageDAO().addOwnerArtistPayment(expoPayment);
        return expoPayment.getAmount();
    }

}
