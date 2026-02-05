package it.unipi.server.model.responses;

public class RegisterResponse {
    
    public enum Status {
        SUCCESS, EMPTYFIELDS, WRONGPASSWORDFORMAT;
    }
    
    private Status status;
    private int matricola;

    public RegisterResponse(Status status, int matricola) {
        this.status = status;
        this.matricola = matricola;
    }

    public int getMatricola() {
        return matricola;
    }

    public Status getStatus() {
        return status;
    }

    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
