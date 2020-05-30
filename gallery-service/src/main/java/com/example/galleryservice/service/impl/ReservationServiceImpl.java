package com.example.galleryservice.service.impl;

import com.example.galleryservice.model.project.Reservation;
import com.example.galleryservice.model.project.ReservationStatus;
import com.example.galleryservice.model.user.User;
import com.example.galleryservice.repository.ClientRepository;
import com.example.galleryservice.repository.ReservationRepository;
import com.example.galleryservice.repository.TicketRepository;
import com.example.galleryservice.repository.UserRepository;
import com.example.galleryservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ReservationServiceImpl(@NotNull final ReservationRepository reservationRepository,
                                  @NotNull final TicketRepository ticketRepository,
                                  @NotNull final ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.ticketRepository = ticketRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Reservation> getAllReservations() {
        List<Reservation> result = reservationRepository.findAll();
        log.info("IN getAllReservations<Reservation> - {} reservations found", result.size());
        return result;
    }

    @Override
    public List<Reservation> findNewReservations() {
        List<Reservation> result = reservationRepository.findReservationsByStatus(ReservationStatus.NEW);
        log.info("IN findNewReservations<Reservation> - {} new reservations found", result.size());
        return result;
    }

    @Override
    public List<Reservation> findPayedReservations() {
        List<Reservation> result = reservationRepository.findReservationsByStatus(ReservationStatus.PAYED);
        log.info("IN findPayedReservations<Reservation> - {} payed reservations found", result.size());
        return result;
    }

    @Override
    public List<Reservation> findClosedReservations() {
        List<Reservation> result = reservationRepository.findReservationsByStatus(ReservationStatus.CLOSED);
        log.info("IN findClosedReservations<Reservation> - {} closed reservations found", result.size());
        return result;
    }

    @Override
    public List<Reservation> findReservationsByClient(@NotNull User user) {
        List<Reservation> result = reservationRepository.findReservationsByClient(user);
        log.info("IN findReservationsByClient<Reservation> - {} reservations found", result.size());
        return result;
    }

    @Override
    public Reservation findReservationById(Long id) {
        Reservation result = reservationRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findReservationById<Reservation> - no reservation found by id: {}", id);
            return null;
        }

        log.info("IN findReservationById<Reservation> - reservation: {} found by id: {}", result, id);
        return result;
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
        log.info("IN deleteReservation<Reservation> - reservation with id: {} successfully deleted", id);
    }

    @Override
    public void deleteReservationsByClient(@NotNull User user) {
        reservationRepository.deleteReservationsByClient(user);
        log.info("IN deleteReservationsByClient<Reservation> - reservations with user: {} successfully deleted", user);
    }
}
