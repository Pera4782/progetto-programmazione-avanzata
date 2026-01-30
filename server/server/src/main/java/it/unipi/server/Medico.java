package it.unipi.server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="medici")
public class Medico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "matricola")
    private int matricola;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "cognome")
    private String cognome;

    @Column(name = "specializzazione")
    private String specializzazione;

    public int getMatricola() {
        return matricola;
    }

    public String getPassword() {
        return password;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }

    public Medico(int matricola, String password, String nome, String cognome, String specializzazione) {
        this.matricola = matricola;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
        this.specializzazione = specializzazione;
    }

    public Medico() {
    }
    
}