package it.unipi.server;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="pazienti")
public class Paziente extends Utente{
    
    public Paziente(int matricola, String password, String nome, String cognome) {
        super(matricola, password, nome, cognome);
    }

    public Paziente() {
    }
    
}
