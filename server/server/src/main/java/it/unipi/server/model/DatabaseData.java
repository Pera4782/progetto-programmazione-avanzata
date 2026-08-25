package it.unipi.server.model;

import java.util.List;


public class DatabaseData {
    private List<Medico> medici;
    private List<Paziente> pazienti;
    private List<Visita> visite;

    public DatabaseData() {}

    public DatabaseData(List<Medico> medici, List<Paziente> pazienti, List<Visita> visite) {
        this.medici = medici;
        this.pazienti = pazienti;
        this.visite = visite;
    }

    public List<Medico> getMedici() { return medici; }
    public void setMedici(List<Medico> medici) { this.medici = medici; }

    public List<Paziente> getPazienti() { return pazienti; }
    public void setPazienti(List<Paziente> pazienti) { this.pazienti = pazienti; }

    public List<Visita> getVisite() { return visite; }
    public void setVisite(List<Visita> visite) { this.visite = visite; }
    
}
