package com.alexandros.teleram.bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "reservations")
public class Reservation {

    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("dateTime")
    private String dateTime;
    @JsonProperty("slot")
    private String slotId;

    @JsonProperty("status")
    private String status;

    public Reservation(String clientName, String dateTime, String slotId, String status) {
        this.clientName = clientName;
        this.dateTime = dateTime;
        this.slotId = slotId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
