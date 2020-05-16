package com.example.galleryservice.user;

import com.example.galleryservice.exceptions.ExpoAlreadyClosedException;
import com.example.galleryservice.project.Artwork;
import com.example.galleryservice.project.Expo;
import com.example.galleryservice.project.Reservation;
import com.example.galleryservice.project.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Owner extends User {

    @NotNull
    private final CorporateAccount corporateAccount;

    private final List<Expo> expos = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    public Owner(final int id,
                 @NotNull final String login,
                 @NotNull final String name,
                 @NotNull final String email,
                 @NotNull final CorporateAccount corporateAccount) {
        super(id, login, name, email);
        this.corporateAccount = corporateAccount;
    }

    public Owner(@NotNull final User user, @NotNull final CorporateAccount corporateAccount) {
        super(user);
        this.corporateAccount = corporateAccount;
    }

    @SneakyThrows
    public List<Ticket> closeReservation(@NotNull final Reservation reservation){
        checkAuthentication();
        if (reservation.isPayed()){
            reservation.setClosed();
            if (LocalDateTime.now().until(reservation.getTickets().get(0).getExpo().getStartTime(), ChronoUnit.MINUTES) > 30){
                return reservation.getTickets();
            } else {
                return new ArrayList<>();
            }
        }
        return null;
    }

    @SneakyThrows
    public void createExpo(@NotNull final String name,
                           @NotNull final String info,
                           @NotNull final LocalDateTime startTime,
                           @NotNull final LocalDateTime endTime,
                           @NotNull final Participant participant){
        checkAuthentication();
        List<Artwork> artworksForExpo = participant.sendArtworks();
        expos.add(new Expo(name, info, participant, startTime, endTime, artworksForExpo));
    }

    @SneakyThrows
    public void editExpo(@NotNull final Expo expo,
                         @NotNull final String argument,
                         @NotNull final String data){
        if (expo.isClosed()){
            throw new ExpoAlreadyClosedException(expo.getName() + "is already closed!");
        }
        checkAuthentication();
        LocalDateTime dataTime;
        switch (argument){
            case "NAME":
                expo.setName(data);
                break;
            case "INFO":
                expo.setInfo(data);
                break;
            case "STARTTIME":
                if (!expo.isStarted()) {
                    dataTime = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    if (dataTime.isBefore(expo.getEndTime())) {
                        expo.setStartTime(dataTime);
                    }
                }
                break;
            case "ENDTIME":
                dataTime = LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                if (dataTime.isAfter(expo.getStartTime())){
                    expo.setStartTime(dataTime);
                }
                break;
            default:
                break;

        }
    }

    @SneakyThrows
    public void startExpo(){
        checkAuthentication();
        for (Expo expo : expos) {
            if (expo.getStartTime().isEqual(LocalDateTime.now()) && !expo.isStarted()){
                expo.setStarted();
            }
        }
    }

    @SneakyThrows
    public void endExpo(){
        checkAuthentication();
        for (Expo expo : expos) {
            if (LocalDateTime.now().isAfter(expo.getEndTime()) && !expo.isClosed()){
                expo.setClosed();
            }
        }
    }

    @SneakyThrows
    public void sendPayment(final double payment, @NotNull final Participant participant){
        checkAuthentication();
        for (Expo expo : expos) {
            if (expo.getArtist().equals(participant) && expo.isClosed()){
                this.corporateAccount.transfer(expo, payment, participant.getPrivateAccount());
            }
        }
    }
}
