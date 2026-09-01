# Progetto Programmazione Avanzata – Gestione Appuntamenti Medici

## 1) Elevator pitch (30 secondi)
Applicazione **client-server** per la gestione di visite mediche, con due ruoli (medico e paziente).  
Il medico crea e gestisce slot di visita; il paziente cerca specialisti e prenota appuntamenti.  
La soluzione usa **JavaFX** lato client, **Spring Boot + Hibernate + MySQL** lato server.

---

## 2) Obiettivo del progetto
Digitalizzare un flusso realistico di prenotazione sanitaria:
- registrazione/login utenti;
- ricerca medici per cognome/specializzazione;
- creazione slot visita da parte del medico;
- prenotazione/annullamento visite da parte del paziente;
- visualizzazione prossimi appuntamenti e pazienti seguiti.

---

## 3) Architettura

### Client (`/client`)
- Desktop app JavaFX (FXML + controller).
- Invoca il server via HTTP JSON (`RequestHandler` con GET/POST/DELETE).
- Gestione sessione locale con `UserSession`.

### Server (`/server/server`)
- API REST con Spring Boot.
- Logica applicativa in `QueryHandler`.
- Persistenza con Hibernate/JPA su MySQL.
- Endpoint principali:
  - `/account` (register/login)
  - `/medico` (ricerca/info medico)
  - `/paziente` (info paziente)
  - `/visita` (crea/prenota/elimina/annulla/ricerca)
  - `/inizializza` (caricamento dataset demo)

### Modello dati
- `Utente` (astratta) → specializzazioni: `Medico`, `Paziente`.
- `Visita` collega medico, paziente, data, ora, tipo, flag `ordinaria`.

---

## 4) Scelte progettuali (parte centrale della presentazione)

1. **Separazione netta client/server**  
   Interfaccia e logica di persistenza sono disaccoppiate: UI più semplice da evolvere e backend riusabile.

2. **REST + JSON come contratto unico**  
   Richieste/risposte tipizzate (`requests`/`responses`) riducono ambiguità tra frontend e backend.

3. **Modello orientato al dominio**  
   Entità mediche esplicite (`Medico`, `Paziente`, `Visita`) rendono il codice leggibile e vicino al problema reale.

4. **Gestione sicurezza minima ma concreta**  
   Password salvate con **BCrypt** (hashing server-side), evitando persistenza in chiaro nel DB.

5. **Regole di business codificate lato server**  
   Esempi: gestione slot ordinari/speciali, vincoli su weekend, prenotazioni duplicate evitate.

6. **Esperienza utente reattiva**  
   Chiamate HTTP eseguite in `Task` JavaFX per non bloccare il thread grafico.

7. **Dataset iniziale per demo rapida**  
   Endpoint `/inizializza` + `dataset.json` permettono di resettare e mostrare subito il sistema in azione.

---

## 5) Flussi funzionali principali

### Paziente
1. Login.
2. Ricerca medico per cognome/specializzazione.
3. Selezione data e slot disponibili.
4. Prenotazione e visualizzazione storico/appuntamenti futuri.
5. Annullamento prenotazione.

### Medico
1. Login.
2. Visualizzazione agenda prossimi appuntamenti.
3. Creazione nuovi slot (ordinari/speciali).
4. Eliminazione slot.
5. Vista sintetica pazienti visitati/da visitare.

---

## 6) Demo pronta in 5 minuti (scaletta)

1. **30s** – problema e obiettivo.
2. **45s** – architettura client/server.
3. **60s** – scelte progettuali (focus su REST, modello dominio, BCrypt).
4. **90s** – demo live: inizializzazione DB → login paziente → prenotazione → login medico e verifica agenda.
5. **55s** – conclusioni: punti forti e possibili estensioni.

---

## 7) Avvio rapido

### Requisiti
- MySQL attivo su `localhost:3306`
- Database: `672602`
- Utente/password DB (configurati): `root` / `root`
- Java 21 per server, Java 11 per client (come da POM)

### Server
```bash
cd /home/runner/work/progetto-programmazione-avanzata/progetto-programmazione-avanzata/server/server
mvn spring-boot:run
```

### Client
```bash
cd /home/runner/work/progetto-programmazione-avanzata/progetto-programmazione-avanzata/client
mvn javafx:run
```

### Inizializzazione dati demo
Dal client: pulsante **“inizializza DB”** nella schermata login (chiama `/inizializza`).

---

## 8) Possibili sviluppi futuri
- autenticazione con token/sessione server-side;
- validazioni e gestione errori REST più standardizzata (codici HTTP dedicati);
- test automatici più estesi su controller e regole di prenotazione;
- containerizzazione (Docker) per setup più rapido.
