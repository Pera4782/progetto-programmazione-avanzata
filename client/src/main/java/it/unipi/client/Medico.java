package it.unipi.client;

import java.io.Serializable;

public class Medico implements Serializable {
    
    private int matricola;
    private String password;
    private String nome;
    private String cognome;
    private String specializzazione;

    public Medico(int matricola, String password, String nome, String cognome, String specializzazione) {
        this.matricola = matricola;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.specializzazione = specializzazione;
    }

    public int getMatricola() { return matricola; }
    public void setMatricola(int matricola) { this.matricola = matricola; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
}