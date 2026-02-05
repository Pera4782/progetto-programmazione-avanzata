package it.unipi.client.model.requests;

import it.unipi.client.model.Medico;
import java.time.LocalDate;
import java.time.LocalTime;

public class CreateVisitaRequest {
    
    private LocalDate date;
    private LocalTime time;
    private String type;
    private Medico medico;
    
    public CreateVisitaRequest() {
    }

    public CreateVisitaRequest(LocalDate date, LocalTime time, String type, Medico medico) {
        this.date = date;
        this.time = time;
        this.type = type;
        this.medico = medico;
    }

    public Medico getMedico() {
        return medico;
    }
    
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
