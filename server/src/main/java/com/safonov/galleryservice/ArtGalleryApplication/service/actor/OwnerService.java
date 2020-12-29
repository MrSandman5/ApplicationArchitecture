package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.CredentialsRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.OwnerRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Credentials;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.logic.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.ReservationModel;
import org.modelmapper.ModelMapper;
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

    private final CredentialsRepository credentialsRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ArtworkRepository artworkRepository;
    private final ClientOwnerPaymentRepository clientOwnerPaymentRepository;
    private final OwnerArtistPaymentRepository ownerArtistPaymentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public OwnerService(@NotNull final CredentialsRepository credentialsRepository,
                        @NotNull final OwnerRepository ownerRepository,
                        @NotNull final ArtistRepository artistRepository,
                        @NotNull final TicketRepository ticketRepository,
                        @NotNull final ReservationRepository reservationRepository,
                        @NotNull final ExpoRepository expoRepository,
                        @NotNull final ArtworkRepository artworkRepository,
                        @NotNull final ClientOwnerPaymentRepository clientOwnerPaymentRepository,
                        @NotNull final OwnerArtistPaymentRepository ownerArtistPaymentRepository,
                        @NotNull final ModelMapper modelMapper) {
        this.credentialsRepository = credentialsRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.expoRepository = expoRepository;
        this.artworkRepository = artworkRepository;
        this.clientOwnerPaymentRepository = clientOwnerPaymentRepository;
        this.ownerArtistPaymentRepository = ownerArtistPaymentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
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
            for (final Artwork artwork : artworkRepository.findArtworksByArtist(artist)) {
                artwork.setExpo(addedExpo);
                artworkRepository.save(artwork);
            }
            return new ResponseEntity<>("", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Expo with name " + existedExpo.getName() + " already created", HttpStatus.ALREADY_REPORTED);
        }
    }

    @Transactional
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
                        || newDateTime.isAfter(curExpo.getStartTime()) && newDateTime.isBefore(curExpo.getEndTime())) {
                    curExpo.setStartTime(newDateTime);
                }
                break;
            case "EndTime":
                newDateTime = LocalDateTime.parse(data, Constants.formatter);
                if (newDateTime.isBefore(curExpo.getEndTime()) && newDateTime.isAfter(curExpo.getStartTime())
                        || newDateTime.isAfter(curExpo.getEndTime())) {
                    curExpo.setEndTime(newDateTime);
                }
                break;
            case "TicketPrice":
                curExpo.setTicketPrice(Double.parseDouble(data));
                break;
            default:
                return new ResponseEntity<>("Wrong data", HttpStatus.BAD_REQUEST);
        }
        expoRepository.save(curExpo);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> startExpo(@NotNull final Long ownerId,
                                            @NotNull final ExpoModel model){
        final Owner owner = ownerRepository.findById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>("Owner doesnt exist", HttpStatus.NOT_FOUND);
        }
        final Expo expo = expoRepository.findExpoByName(model.getName()).orElse(null);
        if (expo == null) {
            return new ResponseEntity<>("Expo doesnt exist", HttpStatus.NOT_FOUND);
        } else if (LocalDateTime.now().isAfter(expo.getStartTime())
                && LocalDateTime.now().isBefore(expo.getEndTime())) {
            expo.setStartTime(LocalDateTime.now());
            expo.setStatus(Constants.ExpoStatus.Opened);
            expoRepository.save(expo);
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Time hasnt come for expo " + expo.getName() + " to start", HttpStatus.GONE);
        }
    }

    @Transactional
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
            openedExpo.setEndTime(LocalDateTime.now());
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
                .stream().filter(reservation -> ticketRepository.findTicketsByReservation(reservation)
                        .stream().findFirst().get().getExpo().equals(closedExpo))
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
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getAllExpos() {
        return new ResponseEntity<>(expoRepository.findAll().stream()
                    .map(expo -> modelMapper.map(expo, ExpoModel.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<Object> getNewExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.New);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos.stream()
                    .map(expo -> modelMapper.map(expo, ExpoModel.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getOpenedExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.Opened);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos.stream()
                    .map(expo -> modelMapper.map(expo, ExpoModel.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<Object> getClosedExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.Closed);
        if (expos == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(expos.stream()
                    .map(expo -> modelMapper.map(expo, ExpoModel.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<Owner> getOwner(@NotNull final Long ownerId) {
        final Credentials credentials = credentialsRepository.findById(ownerId).orElse(null);
        if (credentials == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        final Owner owner = ownerRepository.findByCredentials(credentials).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(owner);
    }

}
