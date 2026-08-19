package it.unipi.server.model.responses;

import it.unipi.server.model.Medico;

public class FindDottoriResponse {

    public enum Status {
        SUCCESS, ERROR;
    }
    
    private Status status;
    private Medico[] medici;

    public FindDottoriResponse() {
    }

    public FindDottoriResponse(Status status, Medico[] medici) {
        this.status = status;
        this.medici = medici;
    }
    
    public Medico[] getMedici() {
        return medici;
    }

    public Status getStatus() {
        return status;
    }

    public void setMedici(Medico[] medici) {
        this.medici = medici;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
