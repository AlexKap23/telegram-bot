package com.alexandros.teleram.bot.services;

import static com.alexandros.teleram.bot.util.Constants.ACCEPTED_MODE;
import static com.alexandros.teleram.bot.util.Constants.ACCEPTED_STATUS;
import static com.alexandros.teleram.bot.util.Constants.PENDING_STATUS;
import static com.alexandros.teleram.bot.util.Constants.REJECTED_STATUS;

import com.alexandros.teleram.bot.dto.ReservationResponseDto;
import com.alexandros.teleram.bot.dto.ResponseDto;
import com.alexandros.teleram.bot.model.Reservation;
import com.alexandros.teleram.bot.repositories.ReservationRepository;
import com.alexandros.teleram.bot.util.ReservationResponseBuilder;
import com.alexandros.teleram.bot.util.RestUtils;
import com.mongodb.Mongo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class BookingReservationService {

    @Autowired
    private RestUtils restUtils;
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MongoOperations mongoOperation;

    Logger logger = LoggerFactory.getLogger(LoggerFactory.class);

    public ResponseDto createNewReservation(ReservationResponseDto payload){
        ResponseDto response = new ResponseDto();
        if(Objects.isNull(payload)){
            response.setCode(400);
            response.setMessage("No payload received");
        }
        try{
            Reservation reservation = new Reservation(payload.getClientName(),payload.getDateTime(),payload.getSlot(),PENDING_STATUS);
            reservationRepository.save(reservation);
            response.setCode(200);
        }catch (Exception e){
            logger.error("Exception caught while adding reservation",e);
            response.setCode(500);
            response.setMessage("Service unavailable");
        }
        return response;
    }

    public ReservationResponseDto findReservationById(String id) {
        if (StringUtils.isBlank(id)) {
            return ReservationResponseBuilder.buildResponse(400,"No reservation id received");
        }
        try {
            Optional<Reservation> optional = reservationRepository.findById(id);
            Reservation reservation = optional.orElse(null);
            if(Objects.isNull(reservation)){
                return ReservationResponseBuilder.buildResponse(404,"No reservation found");
            }
            return ReservationResponseBuilder.buildReservationResponse(200,StringUtils.EMPTY,reservation.getClientName(),reservation.getDateTime(),reservation.getSlotId(),reservation.getId(),reservation.getStatus());
        } catch (Exception e) {
            logger.error("Exception caught while finding reservation by id "+id, e);
            return ReservationResponseBuilder.buildResponse(500,"Service unavailable");
        }
    }

    public ReservationResponseDto approveOrRejectReservation(String reservationId, String mode) {
        if (StringUtils.isEmpty(reservationId)) {
            return ReservationResponseBuilder.buildResponse(400, "No reservation id received");
        }
        try {
            Optional<Reservation> optional = reservationRepository.findById(reservationId);
            Reservation reservation = optional.orElse(null);
            reservation.setStatus(
                StringUtils.isNotEmpty(mode) ? ACCEPTED_MODE.equalsIgnoreCase(mode) ? ACCEPTED_STATUS : REJECTED_STATUS : PENDING_STATUS);
            mongoOperation.save(reservation);
            return ReservationResponseBuilder.buildResponse(200, StringUtils.EMPTY);
        } catch (Exception e) {
            logger.error("Exception caught while updating reservation by id " + reservationId, e);
            return ReservationResponseBuilder.buildResponse(500, "Service unavailable");
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

    public MongoOperations getMongoOperation() {
        return mongoOperation;
    }

    public void setMongoOperation(MongoOperations mongoOperation) {
        this.mongoOperation = mongoOperation;
    }
}
