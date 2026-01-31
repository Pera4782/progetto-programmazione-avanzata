package it.unipi.client;

import java.io.Serializable;

public class Medico extends Utente implements Serializable {
    
    
    private String specializzazione;

    public Medico(int matricola, String password, String nome, String cognome, String specializzazione) {
        super(matricola, password, nome, cognome);
        this.specializzazione = specializzazione;
    }
}