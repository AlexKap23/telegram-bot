package com.alexandros.teleram.bot.controllers;

import com.alexandros.teleram.bot.dto.ReservationDto;
import com.alexandros.teleram.bot.dto.ReservationResponseDto;
import com.alexandros.teleram.bot.model.Reservation;
import com.alexandros.teleram.bot.services.BookingReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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
        ReservationResponseDto responseDto = bookingReservationService.createNewReservation(payload);
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

 /*   @GetMapping(value = "/reservations/{reservation}")
    public Reservation getReservationById(@PathVariable Integer reservation) {
        //TODO find reservation by id
        Reservation reservation1 = new Reservation("1","Alexandros Kapniaris","");
        return reservation1;
    }*/

    public BookingReservationService getReservationService() {
        return bookingReservationService;
    }

    public void setReservationService(BookingReservationService bookingReservationService) {
        this.bookingReservationService = bookingReservationService;
    }
}
