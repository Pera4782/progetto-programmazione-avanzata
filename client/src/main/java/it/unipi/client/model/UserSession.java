package it.unipi.client.model;


public class UserSession {
    
    private static UserSession session;
    
    private int loggedMatricola;
    private Utente loggedUtente;
    
    private UserSession(){}
    
    public static UserSession getSession(){
        if(session == null) session = new UserSession();
        return session;
    }

    
    public void clearSession(){
        loggedMatricola = -1;
        loggedUtente = null;
    }
    
    public int getLoggedMatricola() {
        return loggedMatricola;
    }

    public void setLoggedMatricola(int loggedMatricola) {
        this.loggedMatricola = loggedMatricola;
    }

    public Utente getLoggedUtente() {
        return loggedUtente;
    }

    public void setLoggedUtente(Utente loggedUtente) {
        this.loggedUtente = loggedUtente;
    }
}
