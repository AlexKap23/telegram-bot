package com.alexandros.teleram.bot.dto;

public class ReservationResponseDto {

    private String message;
    private int code;

    public ReservationResponseDto(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ReservationResponseDto(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
