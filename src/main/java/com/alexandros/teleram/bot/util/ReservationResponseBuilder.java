package com.alexandros.teleram.bot.util;

import com.alexandros.teleram.bot.dto.ReservationDto;
import com.alexandros.teleram.bot.dto.ReservationResponseDto;

public class ReservationResponseBuilder {


    public static ReservationResponseDto buildResponse(int code,String message){
        ReservationResponseDto response = new ReservationResponseDto();
        response.setMessage(message);
        response.setCode(code);
        return response;
    }

    public static ReservationResponseDto buildReservationResponse(int code, String message, String clientName, String dateTime, String slot,String id,
        String status) {
        ReservationResponseDto response = new ReservationResponseDto();
        response.setMessage(message);
        response.setCode(code);
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setClientName(clientName);
        reservationDto.setDateTime(dateTime);
        reservationDto.setSlot(slot);
        reservationDto.setStatus(status);
        return response;
    }

}
