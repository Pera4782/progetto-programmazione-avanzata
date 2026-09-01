## Struttura del repository

Il workspace è diviso in due macro-componenti principali:

- `client/`: applicazione front-end JavaFX
- `server/server/`: backend Spring Boot con persistenza tramite Hibernate

Il client gestisce l’interazione con l’utente e le richieste HTTP verso il server, mentre il server contiene la logica applicativa, la persistenza dati e i controller.

### Tree principale

```text
progetto-programmazione-avanzata/
├── client/
│   ├── src/main/java/it/unipi/client/
│   ├── src/main/resources/it/unipi/client/
│   └── pom.xml
├── server/
│   └── server/
│       ├── src/main/java/it/unipi/server/
│       ├── src/main/resources/
│       └── pom.xml
└── README.md
```

---

## Client: struttura e responsabilità

Il modulo client è costruito come un’applicazione JavaFX con una logica di navigazione tra schermate e una serie di controllori che gestiscono la UI.

### Package `it.unipi.client`

Questo package contiene la parte applicativa dell’interfaccia principale.

#### Classi chiave

- `LoginController.java`
  - gestisce login e inizializzazione del database;
  - delega la richiesta HTTP al `RequestHandler`.

- `RegisterController.java`
  - gestisce registrazione di nuovi utenti.

- `PatientsMenuController.java`
  - controller dedicato al paziente;
  - esegue la ricerca di medici per cognome e specializzazione;
  - crea le card dinamiche dei professionisti e le collega alla prenotazione.

- `DoctorsMenuController.java`
  - controller dedicato al medico;
  - gestisce i menu di navigazione, la visualizzazione delle visite e la logica di apertura delle schermate disponibili.

- `BookAppointmentScreen.java`
  - schermata di prenotazione;
  - mostra la disponibilità del medico selezionato;

- `BookedAppointmentScreen.java`
  - schermata dedicata alle visite per ogni medico;
  - mostra le date disponibili e le prenotazioni esistenti;
  - permette di annullare una visita precendentemente creata.

- `ShowPatientsScreen.java`
  - schermata che mostra i pazienti associati a un medico o le relative visite;

- `AppointmentCard.java`
  - componente grafico per rappresentare una singola visita.

- `DoctorCard.java`
  - componente grafico per visualizzare un medico all’interno dei risultati di ricerca.

### Package `it.unipi.client.model`

Questo package rappresenta il modello dati lato client.

Le classi principali sono:

- `Utente`: classe base astratta con nome, cognome, password e matricola.
- `Medico`: estende `Utente` aggiungendo la specializzazione.
- `Paziente`: estende `Utente` senza campi extra.
- `Visita`: rappresenta una visita medica con medico, paziente, data, ora, tipo e flag `ordinaria`.

Le classi in `model.requests` e `model.responses` contengono i DTO usati per le chiamate HTTP:

- `LoginRequest`, `RegisterRequest`, `CreateVisitaRequest`, `BookAppointmentRequest`
- `LoginResponse`, `RegisterResponse`, `GetVisiteResponse`, `FindDottoriResponse`, `Response`

Questo approccio rende il client indipendente dalla struttura interna del server e facilita la serializzazione JSON tramite Gson.

### Package `it.unipi.client.util`

#### `RequestHandler.java`

Questa classe è il punto di accesso centrale per tutte le chiamate HTTP verso il backend.

Responsabilità principali:

- serializzazione e deserializzazione JSON con Gson;
- gestione delle richieste `GET`, `POST` e `DELETE`;

La logica è centralizzata qui per evitare duplicazione di codice nelle classi controller.

#### `MessageHandler.java`

Questa classe gestisce i messaggi visualizzati all’utente tramite l’interfaccia JavaFX. Concentra la gestione di:

- errori;
- messaggi di successo;

### Package `it.unipi.client.session`

#### `UserSession.java`

Gestisce lo stato dell’utente autenticato. È usato per mantenere in memoria:

- la matricola dell’utente loggato;
- l’oggetto utente completo;
- eventuale logout e pulizia sessione.

Questa scelta rende il flusso di navigazione più semplice, evitando di passare sempre lo stato attraverso i controller.

---

## Server: struttura e responsabilità

Il backend Spring Boot è organizzato in modo che ogni controller rappresenti un dominio funzionale della applicazione.

### Package `it.unipi.server.controllers`

#### `AccountController.java`

Questo controller gestisce l’autenticazione e la registrazione.

Responsabilità:

- validazione dei campi obbligatori;
- controllo formato password;
- hashing password via BCrypt;
- chiamata a `QueryHandler` per l’inserimento o la ricerca dell’utente.

##### Endpoint principali

- `POST /account/register`
  - endpoint per la registrazione di un utente, medico o paziente;
  - valida nome, cognome, eventuale specializzazione del medico e formato della password;
  - crea un nuovo `Utente` specifico (`Medico` o `Paziente`) e lo inserisce nel database;
  - restituisce `RegisterResponse` con `SUCCESS`, `EMPTYFIELDS` o `WRONGPASSWORDFORMAT`.

- `POST /account/login`
  - endpoint per il login di un utente sia esso medico o paziente;
  - riceve matricola e password;
  - recupera l’utente tramite `QueryHandler.findUtenteByMatricola(...)`;
  - verifica la password con `BCrypt.checkpw(...)`;
  - restituisce `LoginResponse` relativo allo stato dell’accesso.

#### `VisiteRequestController.java`

Questo controller contiene la logica di accesso alle visite e all’agenda medica. Gli endpoint sono concepiti in modo simile ai metodi della classe: ogni metodo ha una responsabilità precisa, riceve un payload o un parametro e restituisce una risposta standardizzata.

##### Endpoint principali

- `GET /visita/medico`
  - endpoint per cercare le visite di un medico;
  - riceve la matricola del medico come parametro `?_0=<matricola>`;
  - chiama `QueryHandler.getVisiteByMedico(matricola)`;
  - ritorna una `GetVisiteResponse` con `SUCCESS` oppure `ERROR`.

- `POST /visita/elimina`
  - endpoint per eliminare una determinata visita;
  - riceve un oggetto `Visita` come body;
  - delega al metodo `QueryHandler.removeVisita(visita)`;
  - ritorna una `Response` con stato di errore o successo.

- `POST /visita/crea`
  - endpoint per la creazione di una visita da parte di un medico;
  - riceve un `CreateVisitaRequest` con data, ora, tipo, medico e flag `ordinaria`;
  - costruisce un oggetto `Visita` e lo salva tramite `QueryHandler.createVisita(...)`;
  - ritorna una `Response` che rappresenta il risultato dell’operazione.

- `GET /visita/data`
  - endpoint per ottenere le visite con una certa data;
  - riceve la data come parametro `?_0=<yyyy-mm-dd>`;
  - chiama `QueryHandler.getVisiteByData(date)`;
  - restituisce `GetVisiteResponse` con i risultati o un errore.

- `POST /visita/prenota`
  - endpoint per prenotare un appuntamento;
  - riceve un `BookAppointmentRequest` contenente paziente, medico, data e ora;
  - la logica applicativa decide se la visita è ordinaria o non ordinaria e delega il compito a `QueryHandler.bookAppointment(...)`;
  - ritorna `Response` con l’esito della prenotazione.

- `GET /visita/paziente`
  - endpoint per la ricerca degli appuntamenti di un paziente;
  - riceve la matricola del paziente come parametro `?_0=<matricola>`;
  - chiama `QueryHandler.getVisiteByPaziente(matricola)`;
  - restituisce le visite associate al paziente o uno stato di errore.

- `POST /visita/annulla/prenotazione`
  - endpoint per la cancellazione di una prenotazione;
  - riceve un oggetto `Visita` come body;
  - delega la rimozione del legame paziente/visita a `QueryHandler.deleteAppointment(visita)`;
  - ritorna `Response` con valore di successo o errore.

#### `MedicoRequestsController.java`

Controller dedicato alle richieste relative ai medici, con logica di controllo del profilo medico e accesso alle informazioni disponibili.

##### Endpoint principali

- `GET /medico/info`
  - endpoint per ottenere le informazioni di un medico tramite la matricola;
  - riceve la matricola come parametro `?_0=<matricola>`;
  - chiama `QueryHandler.findUtenteByMatricola(matricola, Medico.class)`;
  - restituisce l’oggetto `Medico` trovato oppure `null` in caso di errore.

- `GET /medico/find`
  - endpoint per ottenere i medici filtrati tramite cognome e/o specializzazione;
  - riceve due parametri query: `?_0=<cognome>&_1=<specializzazione>`;
  - se il cognome è nullo, cerca per specializzazione;
  - se la specializzazione è `Qualsiasi`, cerca solo per cognome;
  - altrimenti cerca per entrambe le condizioni contemporaneamente;
  - ritorna un `FindDottoriResponse` con status `SUCCESS` o `ERROR`.

#### `PazienteRequestsController.java`

Controller dedicato alle informazioni del paziente e alla sua parte di dominio.

##### Endpoint principali

- `GET /paziente/info`
  - endpoint per ottenere le informazioni di un paziente tramite la matricola;
  - riceve la matricola come parametro `?_0=<matricola>`;
  - chiama `QueryHandler.findUtenteByMatricola(matricola, Paziente.class)`;
  - restituisce il `Paziente` trovato oppure `null` se non esiste.

#### `InitializeController.java`

Gestisce l’inizializzazione del database in fase di test o bootstrap. In pratica è il punto di accesso per caricare dati iniziali nel sistema.

##### Endpoint principali

- `POST /inizializza`
  - endpoint per inizializzare il database con i dati iniziali del progetto;
  - chiama `QueryHandler.loadDB()`;
  - resetta le tabelle e riempie il database a partire dal file `dataset.json`;
  - restituisce una `Response` che segnala lo stato dell’inizializzazione.

### Package `it.unipi.server.DBHandler`

#### `QueryHandler.java`

Questa è la classe più importante del backend. Contiene quasi tutto il codice di accesso ai dati e la logica del dominio persistente.

Responsabilità principali:

- inizializzazione del database e caricamento di `dataset.json`;
- reset del database;
- inserimento utenti;
- ricerca utenti per matricola;
- ricerca medici per cognome, specializzazione o combinazione di entrambi;
- recupero visite per medico, paziente o data;
- creazione di visite;
- prenotazione di appuntamenti;
- cancellazione di prenotazioni.

La classe usa Hibernate per eseguire query JPA/HQL e organizza le operazioni in metodi dedicati, in modo da mantenere la logica business separata dai controller.

### Package `it.unipi.server.model`

Questo package modella le entità del dominio server-side.

- `Utente` è la superclasse astratta con campi comuni.
- `Medico` e `Paziente` estendono `Utente`.
- `Visita` rappresenta l’unità centrale del sistema.
- `DatabaseData` è un DTO usato per leggere il dataset iniziale dal file JSON.
- `ServerErrorException` centralizza le eccezioni applicative del backend.

Il modello è annotato per JPA/Hibernate, quindi le entità esistono sia come classe Java sia come mapping sul database.

### Package `it.unipi.server.model.requests`

Contiene i payload in ingresso per le chiamate HTTP:

- `LoginRequest`
- `RegisterRequest`
- `CreateVisitaRequest`
- `BookAppointmentRequest`

### Package `it.unipi.server.model.responses`

Contiene i DTO di risposta:

- `LoginResponse`
- `RegisterResponse`
- `GetVisiteResponse`
- `FindDottoriResponse`
- `Response`

Questa separazione facilita la progettazione dei controller e rende chiaro il contratto tra client e server.

### Package `it.unipi.server.util`

#### `HibernateUtil.java`

Configura la session factory di Hibernate. È il punto di partenza per la connessione al database e per la registrazione delle classi mappate.

---

## Relazioni tra client e server

Il flusso di interazione è abbastanza lineare:

1. il client costruisce una richiesta HTTP;
2. `RequestHandler` serializza i dati e invoca l’endpoint;
3. il controller server riceve la richiesta;
4. la logica applicativa chiama `QueryHandler`;
5. `QueryHandler` manipola il database tramite Hibernate;
6. il server ritorna un DTO di risposta;
7. il client decodifica la risposta e aggiorna la UI.

---

## Test rilevanti

Nel backend esistono test JUnit relativi a `QueryHandler`, in particolare per verificare:

- inserimento utenti;
- ricerca per matricola;
- rimozione visite;
- creazione visite;
- ricerca medici per cognome;
- ricerca medici per specializzazione.

Questi test sono utili per verificare che il layer di persistenza e la logica di query mantengano il comportamento atteso.

---
