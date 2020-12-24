package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.OwnerRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ArtworkRepository artworkRepository;
    private final ClientOwnerPaymentRepository clientOwnerPaymentRepository;
    private final OwnerArtistPaymentRepository ownerArtistPaymentRepository;

    @Autowired
    public OwnerService(@NotNull final OwnerRepository ownerRepository,
                        @NotNull final ArtistRepository artistRepository,
                        @NotNull final ReservationRepository reservationRepository,
                        @NotNull final ExpoRepository expoRepository,
                        @NotNull final ArtworkRepository artworkRepository,
                        @NotNull final ClientOwnerPaymentRepository clientOwnerPaymentRepository,
                        @NotNull final OwnerArtistPaymentRepository ownerArtistPaymentRepository) {
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.reservationRepository = reservationRepository;
        this.expoRepository = expoRepository;
        this.artworkRepository = artworkRepository;
        this.clientOwnerPaymentRepository = clientOwnerPaymentRepository;
        this.ownerArtistPaymentRepository = ownerArtistPaymentRepository;
    }

    public ResponseEntity<String> acceptPayment(@NotNull final Long ownerId,
                                                @NotNull final ReservationModel model){
        final Reservation payedReservation = reservationRepository.findById(model.getReservationId()).orElse(null);
        if (payedReservation == null){
            return new ResponseEntity<>("Reservation doesnt exist", HttpStatus.NOT_FOUND);
        } else if (!payedReservation.isPayed()){
            return new ResponseEntity<>("Reservation doesnt payed for", HttpStatus.GONE);
        }
        final Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final ClientOwnerPayment payment = clientOwnerPaymentRepository.findPaymentByReservation(payedReservation).orElse(null);
        if (payment == null){
            return new ResponseEntity<>("Payment for reservation doesnt exist", HttpStatus.NOT_FOUND);
        }
        payedReservation.setStatus(Constants.ReservationStatus.Closed);
        reservationRepository.save(payedReservation);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> createExpo(@NotNull final Long ownerId,
                                             @NotNull final ExpoModel model) {
        final Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Expo existedExpo = expoRepository.findExpoByName(model.getName()).orElse(null);
        if (existedExpo == null) {
            final Artist artist = artistRepository.findById(model.getArtistId()).orElse(null);
            if (artist == null) {
                return new ResponseEntity<>("Artist doesnt exist", HttpStatus.NOT_FOUND);
            }
            final Expo newExpo = new Expo(model.getName(),
                    model.getInfo(),
                    artist,
                    model.getStartTime(),
                    model.getEndTime(),
                    model.getTicketPrice());
            final Expo addedExpo = expoRepository.save(newExpo);
            for (final Artwork artwork : artist.getArtworks()) {
                artwork.setExpo(addedExpo);
                artworkRepository.save(artwork);
            }
            addedExpo.setArtist(artistRepository.save(artist));
            expoRepository.save(addedExpo);
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Expo with name " + existedExpo.getName() + " already created", HttpStatus.ALREADY_REPORTED);
        }
    }

    public ResponseEntity<String> editExpo(@NotNull final Long ownerId,
                                           @NotNull final EditExpoModel model){
        final Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Expo curExpo = expoRepository.findExpoByName(model.getExpo().getName()).orElse(null);
        if (curExpo == null){
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        }
        final LocalDateTime newDateTime;
        final String data = model.getData();
        switch (model.getSettings()){
            case "Name":
                curExpo.setName(data);
                break;
            case "Info":
                curExpo.setInfo(data);
                break;
            case "StartTime":
                newDateTime = LocalDateTime.parse(data, Constants.formatter);
                if (newDateTime.isBefore(curExpo.getStartTime()) && LocalDateTime.now().isBefore(newDateTime)
                        || newDateTime.isAfter(curExpo.getStartTime()) && newDateTime.isBefore(curExpo.getEndTime()))
                    curExpo.setStartTime(newDateTime);
                break;
            case "EndTime":
                newDateTime = LocalDateTime.parse(data, Constants.formatter);
                if (newDateTime.isBefore(curExpo.getEndTime()) && newDateTime.isAfter(curExpo.getStartTime())
                        || newDateTime.isAfter(curExpo.getEndTime()))
                    curExpo.setEndTime(newDateTime);
                break;
            default:
                return new ResponseEntity<>("Wrong data", HttpStatus.BAD_REQUEST);
        }
        expoRepository.save(curExpo);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<String> startExpo(@NotNull final Long ownerId,
                                            @NotNull final ExpoModel model){
        final Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Expo expo = expoRepository.findExpoByName(model.getName()).orElse(null);
        if (expo == null) {
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        } else if ((LocalDateTime.now().isAfter(expo.getStartTime()))
                && LocalDateTime.now().isBefore(expo.getEndTime())) {
            expo.setStatus(Constants.ExpoStatus.Opened);
            expoRepository.save(expo);
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Time hasnt come for expo " + expo.getName() + " to start", HttpStatus.GONE);
        }
    }

    public ResponseEntity<String> closeExpo(@NotNull final Long ownerId,
                                            @NotNull final ExpoModel model){
        final Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Expo openedExpo = expoRepository.findExpoByName(model.getName()).orElse(null);
        if (openedExpo == null) {
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        } else if (!openedExpo.isOpened()) {
            return new ResponseEntity<>("Expo with name " + openedExpo.getName() + " hasnt started!", HttpStatus.NOT_ACCEPTABLE);
        } else if (LocalDateTime.now().isAfter(openedExpo.getStartTime())
                && LocalDateTime.now().isBefore(openedExpo.getEndTime())
                || LocalDateTime.now().isAfter(openedExpo.getEndTime())) {
            openedExpo.setStatus(Constants.ExpoStatus.Closed);
            expoRepository.save(openedExpo);
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Time hasnt come for expo " + openedExpo.getName() + " to close", HttpStatus.GONE);
        }
    }

    @Transactional
    public ResponseEntity<String> payForExpo(@NotNull final Long ownerId,
                                             @NotNull final ExpoModel model){
        final Expo closedExpo = expoRepository.findExpoByName(model.getName()).orElse(null);
        if (closedExpo == null) {
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        } else if (!closedExpo.isClosed()) {
            return new ResponseEntity<>("Expo with name " + closedExpo.getName() + " hasn't closed!", HttpStatus.NOT_ACCEPTABLE);
        }
        final List<Reservation> expoReservations = reservationRepository.findReservationsByStatus(Constants.ReservationStatus.Closed)
                .stream().filter(reservation -> reservation.getTickets().stream().findFirst().get().getExpo().equals(closedExpo))
                .collect(Collectors.toList());
        double payment = 0;
        for (final Reservation entry : expoReservations){
            payment += entry.getCost();
        }
        final Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Artist artist = artistRepository.findById(model.getArtistId()).orElse(null);
        if (artist == null) {
            return new ResponseEntity<>("Artist doesnt exist", HttpStatus.NOT_FOUND);
        }
        final OwnerArtistPayment expoPayment = new OwnerArtistPayment(closedExpo, owner, artist, payment * 0.5);
        ownerArtistPaymentRepository.save(expoPayment);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    public ResponseEntity<Object> getNewExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.New);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos, HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getOpenedExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.Opened);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos, HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getClosedExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.Closed);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos, HttpStatus.OK);
        }
    }

}
