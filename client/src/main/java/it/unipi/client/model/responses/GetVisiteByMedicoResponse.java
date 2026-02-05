package it.unipi.client.model.responses;

import it.unipi.client.model.Visita;

public class GetVisiteByMedicoResponse {
    
    public enum Status {
        SUCCESS, ERROR;
    }
    
    private Status status;
    private Visita[] visite;

    public GetVisiteByMedicoResponse(Status status, Visita[] visite) {
        this.status = status;
        this.visite = visite;
    }

    public GetVisiteByMedicoResponse() {
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
