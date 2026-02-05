package it.unipi.server.model.requests;

public class RegisterRequest {
    private String nome;
    private String cognome;
    private String password;
    private String specializzazione;
    private boolean isDoctor;

    public RegisterRequest() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
    
    public boolean getIsDoctor() { return isDoctor; }
    public void setIsDoctor(boolean isDoctor) { this.isDoctor = isDoctor; }
}
