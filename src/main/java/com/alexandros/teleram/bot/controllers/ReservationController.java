package com.alexandros.teleram.bot.controllers;

import com.alexandros.teleram.bot.dto.ReservationDateTimePayloadDto;
import com.alexandros.teleram.bot.dto.ReservationDto;
import com.alexandros.teleram.bot.dto.ResponseDto;
import com.alexandros.teleram.bot.services.BookingReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private BookingReservationService bookingReservationService;

    @PostMapping(
    value = "/new-reservation",
    consumes = {MediaType.APPLICATION_JSON_VALUE},
    produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addNewReservation(@RequestBody ReservationDto payload) {
        ResponseDto responseDto = bookingReservationService.createNewReservation(payload);
        if(Objects.nonNull(responseDto)){
            if(responseDto.getCode()==200){
                return ResponseEntity.ok(responseDto);
            }else if(responseDto.getCode()==500){
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);
            } else if (responseDto.getCode()==400) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
            }
        }
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(value = "/{reservationId}")
    public ResponseEntity getReservationById(@PathVariable String reservationId) {
        ResponseDto reservation = bookingReservationService.findReservationById(reservationId);
        return getResponseEntity(reservation);
    }

    @PostMapping(
        value = "/handle-reservation/{reservationId}/{mode}")
    public ResponseEntity handleReservation(@PathVariable String reservationId, @PathVariable String mode){
        ResponseDto handle = bookingReservationService.approveOrRejectReservation(reservationId,mode);
        return getResponseEntity(handle);
    }

    @GetMapping(value = "/findAll/{mode}")
    public ResponseEntity getReservations(@PathVariable String mode) {
        ResponseDto reservation = bookingReservationService.findAllByMode(mode);
        return getResponseEntity(reservation);
    }

    @GetMapping(value = "/findByDate")
    public ResponseEntity findReservationsByDate(@RequestBody ReservationDateTimePayloadDto payload) {
        ResponseDto reservation = bookingReservationService.findReservationByDate(payload);
        return getResponseEntity(reservation);
    }

    @GetMapping(value = "/findByTimeSlot")
    public ResponseEntity findByTimeSlot(@RequestBody ReservationDateTimePayloadDto payload) {
        ResponseDto reservation = bookingReservationService.findReservationsAfterDate(payload);
        return getResponseEntity(reservation);
    }

    @NotNull
    private ResponseEntity getResponseEntity(ResponseDto reservation) {
        if(Objects.nonNull(reservation)){
            if(reservation.getCode()==200){
                return ResponseEntity.ok(reservation);
            }else if(reservation.getCode()==500){
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(reservation);
            } else if (reservation.getCode()==400) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(reservation);
            }
        }
        return ResponseEntity.ok().build();
    }

    public BookingReservationService getReservationService() {
        return bookingReservationService;
    }

    public void setReservationService(BookingReservationService bookingReservationService) {
        this.bookingReservationService = bookingReservationService;
    }
}
