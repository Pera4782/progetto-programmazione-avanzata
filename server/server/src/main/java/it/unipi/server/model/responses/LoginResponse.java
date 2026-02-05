package it.unipi.server.model.responses;

public class LoginResponse {
    
    public enum Status {
        SUCCESS, MATRICOLANOTFOUND, WRONGPASSWORD;
    }
    
    private Status status;

    public LoginResponse() {
    }

    public LoginResponse(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
