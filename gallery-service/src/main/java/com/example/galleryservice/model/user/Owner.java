package com.example.galleryservice.model.user;

import com.example.galleryservice.model.project.Expo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "owners")
@Data
public class Owner extends User {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owners")
    private final List<Expo> expos = new ArrayList<>();

    public Owner(@NotNull final String login,
                 @NotNull final String password,
                 @NotNull final String name,
                 @NotNull final String email) {
        super(login, password, name, email);
    }

    public Owner(@NotNull final User user) {
        super(user);
    }

    /*@SneakyThrows
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
                           @NotNull final Artist artist){
        checkAuthentication();
        List<Artwork> artworksForExpo = artist.sendArtworks();
        expos.add(new Expo(name, info, artist, startTime, endTime, artworksForExpo));
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
    public void sendPayment(final double payment, @NotNull final Artist artist){
        checkAuthentication();
        for (Expo expo : expos) {
            if (expo.getArtist().equals(artist) && expo.isClosed()){
                this.corporateAccount.transfer(expo, payment, artist.getPrivateAccount());
            }
        }
    }*/
}
