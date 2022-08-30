package com.alexandros.teleram.bot.controllers;

import com.alexandros.teleram.bot.dto.ResponseDto;
import com.alexandros.teleram.bot.services.BookingReservationService;
import com.alexandros.teleram.bot.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
