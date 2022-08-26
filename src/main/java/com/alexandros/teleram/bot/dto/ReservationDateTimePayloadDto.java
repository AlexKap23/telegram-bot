package com.alexandros.teleram.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationDateTimePayloadDto {
    @JsonProperty("date")
    private String date;
    @JsonProperty("day")
    private int day;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
