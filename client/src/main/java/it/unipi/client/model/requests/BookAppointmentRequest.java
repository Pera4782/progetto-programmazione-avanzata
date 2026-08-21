package it.unipi.client.model.requests;

import it.unipi.client.model.Paziente;
import java.time.LocalDate;
import java.time.LocalTime;


public class BookAppointmentRequest {
    
    private LocalDate date;
    private LocalTime time;
    private Paziente paziente;

    public BookAppointmentRequest() {
    }

    public BookAppointmentRequest(LocalDate date, LocalTime time, Paziente paziente) {
        this.date = date;
        this.time = time;
        this.paziente = paziente;
    }

    public LocalDate getDate() {
        return date;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
