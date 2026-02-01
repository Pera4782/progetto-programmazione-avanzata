package it.unipi.client;

public class LoginRequest {
    
    private int matricola;
    private String password;
    private boolean isDoctor;

    public int getMatricola() {
        return matricola;
    }

    public String getPassword() {
        return password;
    }
    
    public boolean getIsDoctor(){
        return isDoctor;
    }
    
    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsDoctor(boolean isDoctor) {
        this.isDoctor = isDoctor;
    }
    
    public LoginRequest(int matricola, String password, boolean isDoctor) {
        this.matricola = matricola;
        this.password = password;
        this.isDoctor = isDoctor;
    }

    public LoginRequest() {
    }
}
