package com.alexandros.teleram.bot.dto;

import java.util.List;

public class ReservationResponseDto extends ResponseDto{

    private List<ReservationDto> reservations;

    public List<ReservationDto> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationDto> reservations) {
        this.reservations = reservations;
    }
}
