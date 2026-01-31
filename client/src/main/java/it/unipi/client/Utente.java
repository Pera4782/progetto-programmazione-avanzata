package it.unipi.client;


abstract public class Utente {
    private int matricola;
    
    private String password;
    
    private String nome;
    
    private String cognome;

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

    public Utente(int matricola, String password, String nome, String cognome) {
        this.matricola = matricola;
        this.password = password;
        this.nome = nome;
        this.cognome = cognome;
    }

    public Utente() {
    }
    
    
}
