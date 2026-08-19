package it.unipi.client.model;

import it.unipi.client.model.Utente;
import java.io.Serializable;

public class Medico extends Utente implements Serializable {
    
    
    private String specializzazione;

    public Medico(int matricola, String password, String nome, String cognome, String specializzazione) {
        super(matricola, password, nome, cognome);
        this.specializzazione = specializzazione;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }
}