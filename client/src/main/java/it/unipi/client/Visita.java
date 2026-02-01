package it.unipi.client;

import java.time.LocalDate;
import java.time.LocalTime;


public class Visita {
    
    private int id;
    
    private Medico medico;
    
    private Paziente paziente;
    
    private LocalDate data;
    
    private LocalTime ora;

    public LocalDate getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalTime getOra() {
        return ora;
    }

    public Paziente getPaziente() {
        return paziente;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public void setPaziente(Paziente paziente) {
        this.paziente = paziente;
    }

    public Visita(int id, Medico medico, Paziente paziente, LocalDate data, LocalTime ora) {
        this.id = id;
        this.medico = medico;
        this.paziente = paziente;
        this.data = data;
        this.ora = ora;
    }

    public Visita() {
    }
}
