package com.alexandros.teleram.bot.services;

import com.alexandros.teleram.bot.dto.ReservationDto;
import com.alexandros.teleram.bot.dto.ReservationResponseDto;
import com.alexandros.teleram.bot.model.Reservation;
import com.alexandros.teleram.bot.repositories.ReservationRepository;
import com.alexandros.teleram.bot.util.RestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookingReservationService {

    @Autowired
    private RestUtils restUtils;
    @Autowired
    private ReservationRepository reservationRepository;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public ReservationResponseDto createNewReservation(ReservationDto payload){
        ReservationResponseDto response = new ReservationResponseDto();
        if(Objects.isNull(payload)){
            response.setCode(400);
            response.setMessage("No payload received");
        }
        try{
            Reservation reservation = new Reservation(payload.getClientName(),payload.getDateTime(),payload.getSlot());
            reservationRepository.save(reservation);
            response.setCode(200);
        }catch (Exception e){
            logger.error("Exception caught while adding reservation",e);
            response.setCode(500);
            response.setMessage("Service unavailable");
        }
        return response;
    }

    public ReservationDto findReservationById(String id) {
        ReservationDto response = new ReservationDto();
        if (StringUtils.isBlank(id)) {
            response.setCode(400);
            response.setMessage("No reservation id received");
        }
        try {
            Optional<Reservation> optional = reservationRepository.findById(id);
            Reservation reservation = optional.orElse(null);
            if(Objects.isNull(reservation)){
                response.setCode(404);
                response.setMessage("No reservation found");
            }
            response.setCode(200);
            response.setClientName(reservation.getClientName());
            response.setDateTime(reservation.getDateTime());
            response.setSlot(reservation.getSlotId());
            response.setId(reservation.getId());
            return response;
        } catch (Exception e) {
            logger.error("Exception caught while finding reservation by id "+id, e);
            response.setCode(500);
            response.setMessage("Service unavailable");
            return response;
        }
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
