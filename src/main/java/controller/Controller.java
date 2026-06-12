package controller;

import model.*;
import dao.*;
import implementazioneDao.*; // Importa dal tuo pacchetto corretto
import database_connection.ConnessioneDatabase; // Importa dal tuo pacchetto corretto

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {

	private static Controller instance;

	// Rappresentazione in memoria (transiente, rapida per la GUI) (Slide 10)
	private List<Utente> utenti;
	private List<Aula> aule;
	private List<Insegnamento> insegnamenti;
	private List<Lezione> lezioni;
	private List<RichiestaSpostamento> richieste;

	// Riferimenti ai DAO per l'accesso persistente al DB (Slide 7)
	private UtenteDAO utenteDAO;
	private AulaDAO aulaDAO;
	private InsegnamentoDAO insegnamentoDAO;
	private LezioneDAO lezioneDAO;
	private RichiestaSpostamentoDAO richiestaSpostamentoDAO;

	private Utente utenteLoggato;
	private List<Docente> docenti = new ArrayList<>();

	private Controller() {
		utenti = new ArrayList<>();
		aule = new ArrayList<>();
		insegnamenti = new ArrayList<>();
		lezioni = new ArrayList<>();
		richieste = new ArrayList<>();

		// Istanziamo le classi DAO reali con i nomi corretti del tuo progetto
		utenteDAO = new UtenteImplementazionePostgresDAO();
		aulaDAO = new AulaImplementazionePostgresDAO();
		insegnamentoDAO = new InsegnamentoImplementazionePostgresDAO();
		lezioneDAO = new LezioneImplementazionePostgresDAO();
		richiestaSpostamentoDAO = new RichiestaSpostamentoImplementazioneDAO(); // Nome corretto dal tuo pannello

		// Sincronizziamo lo stato all'avvio caricando i dati reali da PostgreSQL (Slide 17)
		sincronizzaConDatabase();
	}

	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}

	// --- AUTENTICAZIONE E LOGGATO DAL DB ---
	public boolean effettuaLogin(String login, String password) {
		try {
			// Verifichiamo le credenziali direttamente sul DB
			String ruolo = utenteDAO.loginDB(login, password);
			if (ruolo != null) {
				// Recuperiamo i dati dell'utente dal DB tramite liste d'appoggio
				ArrayList<Integer> idUtente = new ArrayList<>();
				ArrayList<String> nome = new ArrayList<>();
				ArrayList<String> cognome = new ArrayList<>();
				ArrayList<String> email = new ArrayList<>();
				ArrayList<String> matricola = new ArrayList<>();
				ArrayList<String> annoCorso = new ArrayList<>();
				ArrayList<Boolean> isResponsabile = new ArrayList<>();

				utenteDAO.leggiDatiUtenteLoggatoDB(login, idUtente, nome, cognome, email, matricola, annoCorso, isResponsabile);

				if (!idUtente.isEmpty()) {
					// Istanziamo l'oggetto del Model corretto a seconda del ruolo
					if (ruolo.equals("STUDENTE")) {
						AnnoCorso anno = AnnoCorso.valueOf(annoCorso.get(0));
						Studente s = new Studente(nome.get(0), cognome.get(0), email.get(0), login, password, matricola.get(0), anno);
						s.setIdUtente(idUtente.get(0));
						this.utenteLoggato = s;
					} else if (ruolo.equals("RESPONSABILE")) {
						Responsabile r = new Responsabile(nome.get(0), cognome.get(0), email.get(0), login, password);
						r.setIdUtente(idUtente.get(0));
						this.utenteLoggato = r;
					} else {
						Docente d = new Docente(nome.get(0), cognome.get(0), email.get(0), login, password);
						d.setIdUtente(idUtente.get(0));
						this.utenteLoggato = d;
					}
					return true;
				}
			}
		} catch (SQLException e) {
			System.err.println("Errore durante la procedura di login sul DB.");
			e.printStackTrace();
		}
		return false;
	}

	public void effettuaLogout() {
		this.utenteLoggato = null;
	}

	public Utente getUtenteLoggato() {
		return utenteLoggato;
	}

	// --- METODI DI CONSULTAZIONE IN MEMORIA ---
	public List<Lezione> getLezioniPerStudente(Studente studente) {
		List<Lezione> risultato = new ArrayList<>();
		for (Lezione l : lezioni) {
			if (l.getInsegnamento().getAnnoDiCorso() == studente.getAnno()) {
				risultato.add(l);
			}
		}
		return risultato;
	}

	public List<Lezione> getLezioniPerDocente(Docente docente) {
		List<Lezione> risultato = new ArrayList<>();
		for (Lezione l : lezioni) {
			if (l.getInsegnamento().getDocenteTitolare().getIdUtente() == docente.getIdUtente()) {
				risultato.add(l);
			}
		}
		return risultato;
	}

	public List<RichiestaSpostamento> getRichiesteInAttesa() {
		List<RichiestaSpostamento> risultato = new ArrayList<>();
		for (RichiestaSpostamento r : richieste) {
			if (r.getStato() == StatoRichiesta.IN_ATTESA) {
				risultato.add(r);
			}
		}
		return risultato;
	}

	// --- OPERAZIONI DI SCRITTURA (DB + MEMORIA) ---
	public void inviaRichiestaSpostamento(Lezione lezione, GiornoSettimana nuovoGiorno, String nuovaOraInizio, String nuovaOraFine) {
		try {
			// 1. Scriviamo sul database tramite DAO (Slide 15)
			richiestaSpostamentoDAO.inviaRichiestaDB(lezione.getIdLezione(), nuovoGiorno.toString(), nuovaOraInizio, nuovaOraFine);

			// 2. Aggiorniamo la memoria a runtime per la visualizzazione immediata (Slide 15)
			RichiestaSpostamento nuovaRichiesta = new RichiestaSpostamento(lezione, nuovoGiorno, nuovaOraInizio, nuovaOraFine);
			richieste.add(nuovaRichiesta);
			System.out.println("LOG: Richiesta salvata nel DB e sincronizzata in memoria.");
		} catch (SQLException e) {
			System.err.println("Errore nell'invio della richiesta di spostamento sul DB.");
			e.printStackTrace();
		}
	}

	public void valutaRichiesta(RichiestaSpostamento richiesta, StatoRichiesta nuovoStato) {
		try {
			// 1. Aggiorniamo lo stato della richiesta sul DB
			richiestaSpostamentoDAO.valutaRichiestaDB(richiesta.getIdRichiesta(), nuovoStato.toString());

			// 2. Se approvata, aggiorniamo l'orario della lezione sia sul DB che nel Model
			if (nuovoStato == StatoRichiesta.APPROVATA) {
				Lezione lezione = richiesta.getLezioneDaSpostare();

				// Aggiorniamo su DB
				lezioneDAO.aggiornaOrarioLezioneDB(lezione.getIdLezione(), richiesta.getGiornoProposto().toString(), richiesta.getOraInizioProposta().toString(), richiesta.getOraFineProposta().toString());

				// Aggiorniamo in memoria (Model)
				lezione.setGiorno(richiesta.getGiornoProposto());
				lezione.setOraInizio(richiesta.getOraInizioProposta());
				lezione.setOraFine(richiesta.getOraFineProposta());
			}

			// 3. Modifichiamo lo stato della richiesta in memoria
			richiesta.setStato(nuovoStato);
		} catch (SQLException e) {
			System.err.println("Errore durante la valutazione della richiesta sul DB.");
			e.printStackTrace();
		}
	}

	// --- ALGORITMO DI SINCRONIZZAZIONE ALL'AVVIO ---
	private void sincronizzaConDatabase() {
		try {
			System.out.println("LOG: Avvio sincronizzazione iniziale da PostgreSQL...");

			// IMPORTANTE: Svuotiamo le liste in memoria per evitare duplicazioni in caso di ricaricamento (Slide 17)
			docenti.clear();
			aule.clear();
			insegnamenti.clear();
			lezioni.clear();
			richieste.clear();

			// 1. Carichiamo tutti i Docenti dal DB e popoliamo una mappa temporanea di ID -> Oggetto
			ArrayList<Integer> idDocenti = new ArrayList<>();
			ArrayList<String> nomiDoc = new ArrayList<>();
			ArrayList<String> cognomiDoc = new ArrayList<>();
			ArrayList<String> emailDoc = new ArrayList<>();
			ArrayList<String> loginDoc = new ArrayList<>();
			ArrayList<String> passDoc = new ArrayList<>();
			ArrayList<Boolean> isRespDoc = new ArrayList<>();

			((UtenteImplementazionePostgresDAO) utenteDAO).leggiTuttiIDocentiDB(idDocenti, nomiDoc, cognomiDoc, emailDoc, loginDoc, passDoc, isRespDoc);
			Map<Integer, Docente> mapDocenti = new HashMap<>();
			for (int i = 0; i < idDocenti.size(); i++) {
				Docente d;
				if (isRespDoc.get(i)) {
					d = new Responsabile(nomiDoc.get(i), cognomiDoc.get(i), emailDoc.get(i), loginDoc.get(i), passDoc.get(i));
				} else {
					d = new Docente(nomiDoc.get(i), cognomiDoc.get(i), emailDoc.get(i), loginDoc.get(i), passDoc.get(i));
				}
				d.setIdUtente(idDocenti.get(i));

				// Popoliamo la lista globale e la mappa di supporto
				docenti.add(d);
				mapDocenti.put(idDocenti.get(i), d);
			}

			// 2. Carichiamo tutte le Aule dal DB
			ArrayList<Integer> idAule = new ArrayList<>();
			ArrayList<String> nomiAule = new ArrayList<>();
			aulaDAO.leggiAuleDB(idAule, nomiAule);
			Map<Integer, Aula> mapAule = new HashMap<>();
			for (int i = 0; i < idAule.size(); i++) {
				Aula a = new Aula(nomiAule.get(i));
				a.setIdAula(idAule.get(i));
				aule.add(a);
				mapAule.put(idAule.get(i), a);
			}

			// 3. Carichiamo gli Insegnamenti collegandoli ai rispettivi Docenti titolari
			ArrayList<Integer> idInsegnamenti = new ArrayList<>();
			ArrayList<String> nomiInsegn = new ArrayList<>();
			ArrayList<Integer> cfuList = new ArrayList<>();
			ArrayList<String> anniCorso = new ArrayList<>();
			ArrayList<Integer> fkDocenti = new ArrayList<>();

			insegnamentoDAO.leggiInsegnamentiDB(idInsegnamenti, nomiInsegn, cfuList, anniCorso, fkDocenti);
			Map<Integer, Insegnamento> mapInsegnamenti = new HashMap<>();
			for (int i = 0; i < idInsegnamenti.size(); i++) {
				Docente d = mapDocenti.get(fkDocenti.get(i));
				Insegnamento ins = new Insegnamento(nomiInsegn.get(i), cfuList.get(i), AnnoCorso.valueOf(anniCorso.get(i)), d);
				ins.setIdInsegnamento(idInsegnamenti.get(i));
				insegnamenti.add(ins);
				mapInsegnamenti.put(idInsegnamenti.get(i), ins);
			}

			// 4. Carichiamo le Lezioni collegandole ad Aula e Insegnamento
			ArrayList<Integer> idLezioni = new ArrayList<>();
			ArrayList<Integer> fkInsegnamenti = new ArrayList<>();
			ArrayList<String> giorni = new ArrayList<>();
			ArrayList<String> oreInizio = new ArrayList<>();
			ArrayList<String> oreFine = new ArrayList<>();
			ArrayList<Integer> fkAule = new ArrayList<>();

			lezioneDAO.leggiLezioniDB(idLezioni, fkInsegnamenti, giorni, oreInizio, oreFine, fkAule);
			Map<Integer, Lezione> mapLezioni = new HashMap<>();
			for (int i = 0; i < idLezioni.size(); i++) {
				Insegnamento ins = mapInsegnamenti.get(fkInsegnamenti.get(i));
				Aula a = mapAule.get(fkAule.get(i));
				Lezione lez = new Lezione(ins, GiornoSettimana.valueOf(giorni.get(i)), LocalTime.parse(oreInizio.get(i)), LocalTime.parse(oreFine.get(i)), a);
				lez.setIdLezione(idLezioni.get(i));
				lezioni.add(lez);
				mapLezioni.put(idLezioni.get(i), lez);
			}

			// 5. Carichiamo le Richieste di Spostamento
			ArrayList<Integer> idRichieste = new ArrayList<>();
			ArrayList<Integer> fkLezioni = new ArrayList<>();
			ArrayList<String> giorniProp = new ArrayList<>();
			ArrayList<String> oreInizioProp = new ArrayList<>();
			ArrayList<String> oreFineProp = new ArrayList<>();
			ArrayList<String> stati = new ArrayList<>();

			richiestaSpostamentoDAO.leggiRichiesteDB(idRichieste, fkLezioni, giorniProp, oreInizioProp, oreFineProp, stati);
			for (int i = 0; i < idRichieste.size(); i++) {
				Lezione lez = mapLezioni.get(fkLezioni.get(i));
				RichiestaSpostamento req = new RichiestaSpostamento(lez, GiornoSettimana.valueOf(giorniProp.get(i)), oreInizioProp.get(i), oreFineProp.get(i));
				req.setIdRichiesta(idRichieste.get(i));
				req.setStato(StatoRichiesta.valueOf(stati.get(i)));
				richieste.add(req);
			}

			System.out.println("LOG: Sincronizzazione iniziale completata con successo!");

		} catch (SQLException e) {
			System.err.println("Errore fatale: Impossibile sincronizzare i dati all'avvio dal Database.");
			e.printStackTrace();
		}
	}

	/**
	 * Registra un nuovo utente nel Database e sincronizza lo stato.
	 */
	public boolean registraNuovoUtente(String nome, String cognome, String email, String login, String password, String ruolo, String annoCorso) {
		try {
			utenteDAO.registraUtenteDB(nome, cognome, email, login, password, ruolo, annoCorso);
			sincronizzaConDatabase(); // Riesegue la sincronizzazione per aggiornare le liste in memoria
			return true;
		} catch (SQLException e) {
			System.err.println("Errore durante la registrazione dell'utente nel DB.");
			e.printStackTrace();
			return false;
		}
	}

	// Getters per aule e insegnamenti
	public List<Aula> getAule() {
		return aule;
	}

	public List<Insegnamento> getInsegnamenti() {
		return insegnamenti;
	}

	public List<Docente> getDocenti() {
		return docenti;
	}

	/**
	 * Inserisce una nuova aula sia sul DB che in memoria.
	 */
	public boolean inserisciNuovaAula(String nome) {
		try {
			aulaDAO.inserisciAulaDB(nome);
			sincronizzaConDatabase(); // Ricarica le aule dal DB
			return true;
		} catch (SQLException e) {
			System.err.println("Errore inserimento Aula sul DB.");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Inserisce un nuovo insegnamento sia sul DB che in memoria.
	 */
	public boolean inserisciNuovoInsegnamento(String nome, int cfu, AnnoCorso anno, Docente docente) {
		try {
			int idDocente = (docente != null) ? docente.getIdUtente() : -1;
			insegnamentoDAO.inserisciInsegnamentoDB(nome, cfu, anno.toString(), idDocente);
			sincronizzaConDatabase(); // Ricarica gli insegnamenti dal DB
			return true;
		} catch (SQLException e) {
			System.err.println("Errore inserimento Insegnamento sul DB.");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Inserisce una nuova lezione programmata sia sul DB che nel modello in memoria.
	 */
	public boolean inserisciNuovaLezione(Insegnamento insegnamento, GiornoSettimana giorno, String oraInizio, String oraFine, Aula aula) {
		try {
			// 1. Scriviamo sul DB passando gli identificativi numerici (Slide 15)
			lezioneDAO.inserisciLezioneDB(insegnamento.getIdInsegnamento(), giorno.toString(), oraInizio, oraFine, aula.getIdAula());

			// 2. Ricarichiamo i dati dal DB per sincronizzare le liste
			sincronizzaConDatabase();

			System.out.println("LOG: Nuova lezione inserita sul DB e sincronizzata in memoria.");
			return true;
		} catch (SQLException e) {
			System.err.println("Errore durante l'inserimento della lezione sul DB.");
			e.printStackTrace();
			return false;
		}
	}
} // Chiusura corretta della classe Controller