package com.example.myapplication;

public class TicketSession {
    private static TicketSession instance;
    private int currentTicketId;

    private TicketSession() {}

    public static TicketSession getInstance() {
        if (instance == null) {
            instance = new TicketSession();
        }
        return instance;
    }

    public int getCurrentTicketId() {
        return currentTicketId;
    }

    public void setCurrentTicketId(int ticketId) {
        this.currentTicketId = ticketId;
    }
}
