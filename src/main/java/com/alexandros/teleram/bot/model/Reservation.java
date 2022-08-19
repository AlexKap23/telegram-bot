package com.alexandros.teleram.bot.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import javax.persistence.Id;

@Document(collection = "botinfo")
public class Reservation {

    @Id
    private String id;
    private String clientName;
    private Date dateTime;

    public Reservation(String id, String clientName, Date dateTime) {
        this.id = id;
        this.clientName = clientName;
        this.dateTime = dateTime;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
