package it.unipi.client.model;

import java.time.LocalDate;
import java.time.LocalTime;


public class Visita {
    
    private int id;
    
    private Medico medico;
    
    private Paziente paziente;
    
    private LocalDate data;
    
    private LocalTime ora;
    
    private String tipo;

    public String getTipo() {
        return tipo;
    }

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

    public String getNomePaziente(){
        
        if(paziente == null) return "-";
        return paziente.getNome() + " " + paziente.getCognome();
    }
    
    public Integer getMatricolaPaziente(){
        if(paziente == null) return -1;
        return paziente.getMatricola();
    }
    
    public String getStatusVisita(){
        
        LocalDate oggi = LocalDate.now();
        
        if(data.isBefore(oggi)) return "Completata";
        return "Da Fare";
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

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public Visita(int id, Medico medico, Paziente paziente, LocalDate data, LocalTime ora, String tipo) {
        this.id = id;
        this.medico = medico;
        this.paziente = paziente;
        this.data = data;
        this.ora = ora;
        this.tipo = tipo;
    }

    public Visita() {
    }
}
