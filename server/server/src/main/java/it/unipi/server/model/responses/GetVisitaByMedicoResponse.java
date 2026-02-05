package it.unipi.server.model.responses;

import it.unipi.server.model.Visita;

public class GetVisitaByMedicoResponse {
    
    public enum Status {
        SUCCESS, ERROR;
    }
    
    private Status status;
    private Visita[] visite;

    public GetVisitaByMedicoResponse(Status status, Visita[] visite) {
        this.status = status;
        this.visite = visite;
    }

    public GetVisitaByMedicoResponse() {
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
