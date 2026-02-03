package it.unipi.server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "visite")
public class Visita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "medico", referencedColumnName = "matricola", nullable = false)
    private Medico medico;
    
    @ManyToOne
    @JoinColumn(name = "paziente", referencedColumnName = "matricola", nullable = false)
    private Paziente paziente;
    
    @Column(name = "data")
    private LocalDate data;
    
    @Column(name = "ora")
    private LocalTime ora;
    
    @Column(name = "tipo")
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
