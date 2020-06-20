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

    @NotNull
    private List<String> expos = new ArrayList<>();

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
    public double acceptPayment(final long reservation){
        this.checkAuthentication();
        final Reservation payedReservation = getStorageDAO().getReservation(reservation);
        if (payedReservation == null){
            throw new ReservationNotFoundException(reservation);
        } else if (!payedReservation.isPayed()){
            throw new ReservationNotPaidException(payedReservation.getId());
        }
        final ClientOwnerPayment payment = getStorageDAO().getClientOwnerPaymentByReservation(payedReservation.getId());
        payedReservation.setStatus(ReservationStatus.Closed);
        getStorageDAO().updateReservation(payedReservation);
        return payment.getPrice();
    }

    @SneakyThrows
    public Expo createExpo(@NotNull final String name,
                           @NotNull final String info,
                           final long artist,
                           @NotNull final LocalDateTime startTime,
                           @NotNull final LocalDateTime endTime,
                           final double ticketPrice,
                           @NotNull final List<String> artworks){
        this.checkAuthentication();
        final Expo existedExpo = getStorageDAO().getExpo(name);
        if (existedExpo == null) {
            final Expo newExpo = new Expo(name, info, artist, startTime, endTime, ticketPrice, artworks);
            final long expoId = getStorageDAO().addExpo(newExpo);
            final Expo addedExpo = getStorageDAO().getExpo(expoId);
            this.expos.add(addedExpo.getName());
            return addedExpo;
        }
        if (expos.contains(existedExpo.getName()))
            throw new ExpoAlreadyExistedException("Expo with id : " + existedExpo.getId() + " already existed for this user!");
        this.expos.add(existedExpo.getName());
        getStorageDAO().updateOwner(this);
        return existedExpo;
    }

    @SneakyThrows
    public Expo createExpo(@NotNull final Expo expo){
        this.checkAuthentication();
        if (expos.contains(expo.getName()))
            throw new ExpoAlreadyExistedException("Expo with id : " + expo.getId() + " already existed for this user!");
        final Expo existedExpo = getStorageDAO().getExpo(expo.getName());
        if (existedExpo == null) {
            final long expoId = getStorageDAO().addExpo(expo);
            final Expo addedExpo = getStorageDAO().getExpo(expoId);
            this.expos.add(addedExpo.getName());
            return addedExpo;
        }
        this.expos.add(existedExpo.getName());
        getStorageDAO().updateOwner(this);
        return existedExpo;
    }

    @SneakyThrows
    public void editExpo(@NotNull final String expo,
                         @NotNull final EditSettings settings,
                         @NotNull final String data){
        this.checkAuthentication();
        final Expo curExpo = getStorageDAO().getExpo(expo);
        if (curExpo == null){
            throw new ExpoNotFoundException(expo);
        }
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
        getStorageDAO().updateExpo(curExpo);
    }

    @SneakyThrows
    public void startExpo(@NotNull final String expo){
        this.checkAuthentication();
        final Expo newExpo = getStorageDAO().getExpo(expo);
        if (newExpo == null){
            throw new ExpoNotFoundException(expo);
        } else if ((LocalDateTime.now().isAfter(newExpo.getStartTime()))
                && LocalDateTime.now().isBefore(newExpo.getEndTime())) {
            newExpo.setStatus(ExpoStatus.Opened);
            getStorageDAO().updateExpo(newExpo);
        }
    }

    @SneakyThrows
    public void closeExpo(@NotNull final String expo){
        this.checkAuthentication();
        final Expo openedExpo = getStorageDAO().getExpo(expo);
        if (openedExpo == null){
            throw new ExpoNotFoundException(expo);
        } else if (!openedExpo.isOpened()) {
            throw new ExpoHasNotOpenedException("Expo with id : " + openedExpo.getId() + " hasn't started!");
        } else if (LocalDateTime.now().isAfter(openedExpo.getStartTime())
                && LocalDateTime.now().isBefore(openedExpo.getEndTime())
                || LocalDateTime.now().isAfter(openedExpo.getEndTime())) {
            openedExpo.setStatus(ExpoStatus.Closed);
            getStorageDAO().updateExpo(openedExpo);
        }
    }

    @SneakyThrows
    public double payForExpo(@NotNull final String expo){
        this.checkAuthentication();
        final Expo closedExpo = getStorageDAO().getExpo(expo);
        if (closedExpo == null) {
            throw new ExpoNotFoundException(expo);
        } else if (!closedExpo.isClosed()) {
            throw new ExpoHasNotClosedException("Expo with id : " + closedExpo.getId() + " hasn't closed!");
        }
        final List<Reservation> expoReservations = getStorageDAO().getReservationsByStatus(ReservationStatus.Closed.toString())
                .stream().filter(reservation -> getStorageDAO().getTicket(reservation.getTickets().get(0)).getExpo() == closedExpo.getId())
                .collect(Collectors.toList());
        double payment = 0;
        for (Reservation entry : expoReservations){
            payment += entry.getCost();
        }
        final OwnerArtistPayment expoPayment = new OwnerArtistPayment(closedExpo.getId(), this.getId(), closedExpo.getArtist(), payment * 0.5);
        final long ownerArtistPaymentId = getStorageDAO().addOwnerArtistPayment(expoPayment);
        this.expos.remove(closedExpo.getName());
        getStorageDAO().updateOwner(this);
        return getStorageDAO().getPayment(ownerArtistPaymentId).getPrice();
    }

}
