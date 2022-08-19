package com.alexandros.teleram.bot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/reservations")
public class ReservationController {

    @PostMapping
    @RequestMapping("/new-reservation")
    public ModelAndView addNewReservation() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("welcome");
        mv.getModel().put("data", "Welcome home man");
        return mv;
    }
}
