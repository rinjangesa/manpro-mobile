package com.jc.allnewjcclass.entities;

import java.util.Date;

public class Message {

    private String id;
    private String to;
    private String from;
    private String message;
    private Date sentdate;
    private boolean read;

    public Message() {
    }

    public Message(String id, String to, String from, String message, Date sentdate, boolean read) {
        this.id = id;
        this.to = to;
        this.from = from;
        this.message = message;
        this.sentdate = sentdate;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSentdate() {
        return sentdate;
    }

    public void setSentdate(Date sentdate) {
        this.sentdate = sentdate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
