package it.unipi.client.model.responses;

import it.unipi.client.model.Visita;

public class GetVisiteResponse {
    
    public enum Status {
        SUCCESS, ERROR;
    }
    
    private Status status;
    private Visita[] visite;

    public GetVisiteResponse(Status status, Visita[] visite) {
        this.status = status;
        this.visite = visite;
    }

    public GetVisiteResponse() {
    }

    public Status getStatus() {
        return status;
    }

    public Visita[] getVisite() {
        return visite;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setVisite(Visita[] visite) {
        this.visite = visite;
    }
}
