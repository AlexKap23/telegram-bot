package com.alexandros.teleram.bot.controllers;

import com.alexandros.teleram.bot.dto.ResponseDto;
import com.alexandros.teleram.bot.dto.SlotDto;
import com.alexandros.teleram.bot.services.BookingReservationService;
import com.alexandros.teleram.bot.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/slots")
public class SlotController {

    @Autowired
    private BookingReservationService bookingReservationService;

    @Autowired
    private RestUtils restUtils;

    @GetMapping(value = "/all-slots")
    public ResponseEntity getReservationById() {
        ResponseDto reservation = bookingReservationService.findSlots();
        return getRestUtils().getResponseEntity(reservation);
    }

    @PostMapping(
        value = "/new-slot",
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addNewSlot(@RequestBody SlotDto payload) {
        ResponseDto responseDto = bookingReservationService.createNewSlot(payload);
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

    public BookingReservationService getBookingReservationService() {
        return bookingReservationService;
    }

    public void setBookingReservationService(BookingReservationService bookingReservationService) {
        this.bookingReservationService = bookingReservationService;
    }

    public RestUtils getRestUtils() {
        return restUtils;
    }

    public void setRestUtils(RestUtils restUtils) {
        this.restUtils = restUtils;
    }
}
