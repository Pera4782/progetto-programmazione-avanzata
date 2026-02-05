package it.unipi.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="medici")
public class Medico extends Utente{
    

    @Column(name = "specializzazione")
    private String specializzazione;

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }
    
    
    public Medico(int matricola, String password, String nome, String cognome, String specializzazione) {
        super(matricola, password, nome, cognome);
        this.specializzazione = specializzazione;
    }

    public Medico() {
    }
    
}