package it.unipi.server.model.requests;

import it.unipi.server.model.Medico;
import it.unipi.server.model.Paziente;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookAppointmentRequest {
    
    private LocalDate date;
    private LocalTime time;
    private Paziente paziente;
    private Medico medico;

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

    public Medico getMedico() {
        return medico;
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

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
    
}

