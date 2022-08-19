package com.alexandros.teleram.bot.controllers;

import com.alexandros.teleram.bot.model.Reservation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import javax.ws.rs.core.Response;

@RestController
public class ReservationController {

    @PostMapping
    @RequestMapping("/reservations/new-reservation")
    public Response addNewReservation(@RequestBody String payload) {
        System.out.println(payload);
        return Response.ok(payload).build();
    }

    @GetMapping(value = "/reservations/{reservation}")
    public Reservation getReservationById(@PathVariable Integer reservation) {
        //TODO find reservation by id
        Reservation reservation1 = new Reservation("1","Alexandros Kapniaris",new Date(System.currentTimeMillis()));
        return reservation1;
    }
}
