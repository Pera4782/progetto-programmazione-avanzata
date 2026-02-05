package it.unipi.client.model;

import it.unipi.client.model.Utente;
import java.io.Serializable;


public class Paziente extends Utente implements Serializable{
    
    public Paziente(int matricola, String password, String nome, String cognome) {
        super(matricola, password, nome, cognome);
    }

    public Paziente() {
        super();
    }
    
}
