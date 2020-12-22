package com.safonov.galleryservice.ArtGalleryApplication.service.actor;

import com.safonov.galleryservice.ArtGalleryApplication.configuration.Constants;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ArtistRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.ClientRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.actor.OwnerRepository;
import com.safonov.galleryservice.ArtGalleryApplication.data.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Artist;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Client;
import com.safonov.galleryservice.ArtGalleryApplication.entity.actor.Owner;
import com.safonov.galleryservice.ArtGalleryApplication.entity.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.gallery.*;
import com.safonov.galleryservice.ArtGalleryApplication.model.info.ExpoModel;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ApiResponse;
import com.safonov.galleryservice.ArtGalleryApplication.model.response.ResponseOrMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final ClientRepository clientRepository;
    private final OwnerRepository ownerRepository;
    private final ArtistRepository artistRepository;
    private final ReservationRepository reservationRepository;
    private final ExpoRepository expoRepository;
    private final ArtworkRepository artworkRepository;
    private final ClientOwnerPaymentRepository clientOwnerPaymentRepository;
    private final OwnerArtistPaymentRepository ownerArtistPaymentRepository;

    @Autowired
    OwnerService(@NotNull final ClientRepository clientRepository,
                 @NotNull final OwnerRepository ownerRepository,
                 @NotNull final ArtistRepository artistRepository,
                 @NotNull final ReservationRepository reservationRepository,
                 @NotNull final ExpoRepository expoRepository,
                 @NotNull final ArtworkRepository artworkRepository,
                 @NotNull final ClientOwnerPaymentRepository clientOwnerPaymentRepository,
                 @NotNull final OwnerArtistPaymentRepository ownerArtistPaymentRepository) {
        this.clientRepository = clientRepository;
        this.ownerRepository = ownerRepository;
        this.artistRepository = artistRepository;
        this.reservationRepository = reservationRepository;
        this.expoRepository = expoRepository;
        this.artworkRepository = artworkRepository;
        this.clientOwnerPaymentRepository = clientOwnerPaymentRepository;
        this.ownerArtistPaymentRepository = ownerArtistPaymentRepository;
    }

    public ApiResponse acceptPayment(@NotNull final AcceptPaymentModel model){
        final Reservation payedReservation = reservationRepository.findById(model.getReservation().getReservationId()).orElse(null);
        if (payedReservation == null){
            return new ApiResponse("Reservation doesnt exist");
        } else if (!payedReservation.isPayed()){
            return new ApiResponse("Reservation doesnt payed for");
        }
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ApiResponse("Owner doesnt exist");
        } else if (!owner.getAuthenticated()) {
            return new ApiResponse("Owner wasnt authenticated");
        }
        final ClientOwnerPayment payment = clientOwnerPaymentRepository.findPaymentByReservation(payedReservation).orElse(null);
        if (payment == null){
            return new ApiResponse("Payment for reservation doesnt exist");
        }
        payedReservation.setStatus(Constants.ReservationStatus.Closed);
        reservationRepository.save(payedReservation);
        return new ApiResponse("Owner " + owner.getCredentials().getLogin() + " accepted payment for reservation");
    }

    @Transactional
    public ApiResponse createExpo(@NotNull final CreateExpoModel model) {
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ApiResponse("Owner doesnt exist");
        } else if (!owner.getAuthenticated()) {
            return new ApiResponse("Owner wasnt authenticated");
        }
        final Expo existedExpo = expoRepository.findById(model.getExpo().getExpoId()).orElse(null);
        if (existedExpo == null) {
            final ExpoModel expoModel = model.getExpo();
            final Artist artist = artistRepository.findById(expoModel.getArtistId()).orElse(null);
            if (artist == null) {
                return new ApiResponse("Artist doesnt exist");
            }
            final Expo newExpo = new Expo(expoModel.getName(),
                    expoModel.getInfo(),
                    artist,
                    expoModel.getStartTime(),
                    expoModel.getEndTime(),
                    expoModel.getTicketPrice());
            final Expo addedExpo = expoRepository.save(newExpo);
            for (final Artwork artwork : artist.getArtworks()) {
                artwork.setExpo(addedExpo);
                artworkRepository.save(artwork);
            }
            addedExpo.setArtist(artistRepository.save(artist));
            expoRepository.save(addedExpo);
            return new ApiResponse("New expo with name " + newExpo.getName() + "was created");
        } else {
            return new ApiResponse("Expo with name " + existedExpo.getName() + " already created");
        }
    }

    public ApiResponse editExpo(@NotNull final EditExpoModel model){
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ApiResponse("Owner doesnt exist");
        } else if (!owner.getAuthenticated()) {
            return new ApiResponse("Owner wasnt authenticated");
        }
        final Expo curExpo = expoRepository.findById(model.getExpo().getExpoId()).orElse(null);
        if (curExpo == null){
            return new ApiResponse("Expo doesnt exist");
        }
        final LocalDateTime newDateTime;
        final String data = model.getData();
        switch (model.getSettings()){
            case Name:
                curExpo.setName(data);
                break;
            case Info:
                curExpo.setInfo(data);
                break;
            case StartTime:
                newDateTime = LocalDateTime.parse(data, Constants.formatter);
                if (newDateTime.isBefore(curExpo.getStartTime()) && LocalDateTime.now().isBefore(newDateTime)
                        || newDateTime.isAfter(curExpo.getStartTime()) && newDateTime.isBefore(curExpo.getEndTime()))
                    curExpo.setStartTime(newDateTime);
                break;
            case EndTime:
                newDateTime = LocalDateTime.parse(data, Constants.formatter);
                if (newDateTime.isBefore(curExpo.getEndTime()) && newDateTime.isAfter(curExpo.getStartTime())
                        || newDateTime.isAfter(curExpo.getEndTime()))
                    curExpo.setEndTime(newDateTime);
                break;
            default:
                return new ApiResponse("Wrong data");
        }
        expoRepository.save(curExpo);
        return new ApiResponse("Expo with id " + curExpo.getId() + " successfully updated");
    }

    public ApiResponse startExpo(@NotNull final StartCloseExpoModel model){
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ApiResponse("Owner doesnt exist");
        } else if (!owner.getAuthenticated()) {
            return new ApiResponse("Owner wasnt authenticated");
        }
        final Expo expo = expoRepository.findById(model.getExpo().getExpoId()).orElse(null);
        if (expo == null) {
            return new ApiResponse("Expo doesnt exist");
        } else if ((LocalDateTime.now().isAfter(expo.getStartTime()))
                && LocalDateTime.now().isBefore(expo.getEndTime())) {
            expo.setStatus(Constants.ExpoStatus.Opened);
            expoRepository.save(expo);
            return new ApiResponse("Expo " + expo.getName() + " successfully started");
        } else {
            return new ApiResponse("Time hasnt come for expo " + expo.getName() + " to start");
        }
    }

    public ApiResponse closeExpo(@NotNull final StartCloseExpoModel model){
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ApiResponse("Owner doesnt exist");
        } else if (!owner.getAuthenticated()) {
            return new ApiResponse("Owner wasnt authenticated");
        }
        final Expo openedExpo = expoRepository.findById(model.getExpo().getExpoId()).orElse(null);
        if (openedExpo == null) {
            return new ApiResponse("Expo doesnt exist");
        } else if (!openedExpo.isOpened()) {
            return new ApiResponse("Expo with name " + openedExpo.getName() + " hasnt started!");
        } else if (LocalDateTime.now().isAfter(openedExpo.getStartTime())
                && LocalDateTime.now().isBefore(openedExpo.getEndTime())
                || LocalDateTime.now().isAfter(openedExpo.getEndTime())) {
            openedExpo.setStatus(Constants.ExpoStatus.Closed);
            expoRepository.save(openedExpo);
            return new ApiResponse("Expo " + openedExpo.getName() + " successfully closed");
        } else {
            return new ApiResponse("Time hasnt come for expo " + openedExpo.getName() + " to close");
        }
    }

    @Transactional
    public ApiResponse payForExpo(@NotNull final PayForExpoModel model){
        final Expo closedExpo = expoRepository.findById(model.getExpo().getExpoId()).orElse(null);
        if (closedExpo == null) {
            return new ApiResponse("Expo doesnt exist");
        } else if (!closedExpo.isClosed()) {
            return new ApiResponse("Expo with name " + closedExpo.getName() + " hasn't closed!");
        }
        final List<Reservation> expoReservations = reservationRepository.findReservationsByStatus(Constants.ReservationStatus.Closed)
                .stream().filter(reservation -> reservation.getTickets().get(0).getExpo().equals(closedExpo))
                .collect(Collectors.toList());
        double payment = 0;
        for (final Reservation entry : expoReservations){
            payment += entry.getCost();
        }
        final Owner owner = ownerRepository.findById(model.getOwnerId()).orElse(null);
        if (owner == null) {
            return new ApiResponse("Owner doesnt exist");
        } else if (!owner.getAuthenticated()) {
            return new ApiResponse("Owner wasnt authenticated");
        }
        final Artist artist = artistRepository.findById(model.getExpo().getArtistId()).orElse(null);
        if (artist == null) {
            return new ApiResponse("Artist doesnt exist");
        }
        final OwnerArtistPayment expoPayment = new OwnerArtistPayment(closedExpo, owner, artist, payment * 0.5);
        ownerArtistPaymentRepository.save(expoPayment);
        return new ApiResponse("Owner " + owner.getCredentials().getLogin() + " send royalties for expo " + closedExpo.getName());
    }

    public ResponseOrMessage<List<Expo>> getNewExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.New);
        if (expos == null) {
            return new ResponseOrMessage<>("There are no new expos");
        } else {
            return new ResponseOrMessage<>(expos);
        }
    }

    public ResponseOrMessage<List<Expo>> getOpenedExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.Opened);
        if (expos == null) {
            return new ResponseOrMessage<>("There are no opened expos");
        } else {
            return new ResponseOrMessage<>(expos);
        }
    }

    public ResponseOrMessage<List<Expo>> getClosedExpos() {
        final List<Expo> expos = expoRepository.findExposByStatus(Constants.ExpoStatus.Closed);
        if (expos == null) {
            return new ResponseOrMessage<>("There are no closed expos");
        } else {
            return new ResponseOrMessage<>(expos);
        }
    }

    /*public ApiResponse deletePerson(@NotNull final Long userId, @NotNull final String userType) {
        switch (userType) {
            case "ROLE_ARTIST":
                final Artist artist = artistRepository.findById(userId).orElse(null);
                if (artist == null) {
                    return new ApiResponse("Artist not found");
                }
                artistRepository.save(artist);
                return new ApiResponse("Artist was deleted");
            case "ROLE_CLIENT":
                final Client client = clientRepository.findById(userId).orElse(null);
                if (client == null) {
                    return new ApiResponse("Client not found");
                }
                clientRepository.save(client);
                return new ApiResponse("Client was deleted");

            default:
                return new ApiResponse("Wrong parameter");
        }
    }*/

}
