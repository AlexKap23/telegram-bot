package com.alexandros.teleram.bot.services;

import com.alexandros.teleram.bot.dto.ReservationDto;
import com.alexandros.teleram.bot.dto.ReservationResponseDto;
import com.alexandros.teleram.bot.model.Reservation;
import com.alexandros.teleram.bot.model.Slot;
import com.alexandros.teleram.bot.repositories.ReservationRepository;
import com.alexandros.teleram.bot.util.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BookingReservationService {

    @Autowired
    private RestUtils restUtils;
    @Autowired
    private ReservationRepository reservationRepository;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public ReservationResponseDto createNewReservation(ReservationDto payload){
        if(Objects.isNull(payload)) return new ReservationResponseDto("No payload received",400);
        try{
            Reservation reservation = new Reservation(payload.getClientName(),payload.getDateTime(),payload.getSlot());
            reservationRepository.save(reservation);
        }catch (Exception e){
            logger.error("Exception caught while adding reservation",e);
            return new ReservationResponseDto("No payload received",500);
        }
        return new ReservationResponseDto(200);
    }

    public RestUtils getRestUtils() {
        return restUtils;
    }

    public void setRestUtils(RestUtils restUtils) {
        this.restUtils = restUtils;
    }

    public ReservationRepository getReservationRepository() {
        return reservationRepository;
    }

    public void setReservationRepository(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
}
