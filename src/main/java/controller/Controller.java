package controller;

import model.*;
import model.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Controller {


	private static Controller instance;


	private List<Utente> utenti;
	private List<Aula> aule;
	private List<Insegnamento> insegnamenti;
	private List<Lezione> lezioni;
	private List<RichiestaSpostamento> richieste;


	private Utente utenteLoggato;


	private Controller() {
		utenti = new ArrayList<>();
		aule = new ArrayList<>();
		insegnamenti = new ArrayList<>();
		lezioni = new ArrayList<>();
		richieste = new ArrayList<>();

		caricaDatiDiProva();
	}


	public static Controller getInstance() {
		if (instance == null) {
			instance = new Controller();
		}
		return instance;
	}



	public boolean effettuaLogin(String login, String password) {
		for (Utente u : utenti) {
			if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
				this.utenteLoggato = u;
				return true;
			}
		}
		return false;
	}

	public void effettuaLogout() {
		this.utenteLoggato = null;
	}

	public Utente getUtenteLoggato() {
		return utenteLoggato;
	}

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
			if (l.getInsegnamento().getDocenteTitolare().equals(docente)) {
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

	public void inviaRichiestaSpostamento(Lezione lezione, GiornoSettimana nuovoGiorno, String nuovaOraInizio, String nuovaOraFine) {
		RichiestaSpostamento nuovaRichiesta = new RichiestaSpostamento(lezione, nuovoGiorno, nuovaOraInizio, nuovaOraFine);
		richieste.add(nuovaRichiesta);
		System.out.println("LOG: Richiesta salvata nel sistema.");
	}

	// UNICO METODO DI VALUTAZIONE (Avanzato: aggiorna l'orario reale a seguito di approvazione)
	public void valutaRichiesta(RichiestaSpostamento richiesta, StatoRichiesta nuovoStato) {
		richiesta.setStato(nuovoStato);
		if (nuovoStato == StatoRichiesta.APPROVATA) {
			Lezione lezione = richiesta.getLezioneDaSpostare();
			lezione.setGiorno(richiesta.getGiornoProposto());
			lezione.setOraInizio(richiesta.getOraInizioProposta());
			lezione.setOraFine(richiesta.getOraFineProposta());
			System.out.println("LOG: Orario della lezione aggiornato con successo a seguito dell'approvazione.");
		}
	}


	private void caricaDatiDiProva() {

		Studente s1 = new Studente("Mario", "Rossi", "mario@studenti.it", "studente", "1234", "DE10000", AnnoCorso.II_ANNO);
		Docente d1 = new Docente("Ada", "Lovelace", "ada@docenti.it", "docente", "1234");
		Responsabile r1 = new Responsabile("Alan", "Turing", "alan@admin.it", "admin", "admin");

		utenti.add(s1);
		utenti.add(d1);
		utenti.add(r1);


		Aula a1 = new Aula("Aula A1");
		aule.add(a1);

		Insegnamento progOgg = new Insegnamento("Programmazione Oggetti", 9, AnnoCorso.II_ANNO, d1);
		insegnamenti.add(progOgg);


		Lezione l1 = new Lezione(progOgg, GiornoSettimana.LUNEDI, LocalTime.of(10, 0), LocalTime.of(12, 0), a1);
		Lezione l2 = new Lezione(progOgg, GiornoSettimana.GIOVEDI, LocalTime.of(14, 0), LocalTime.of(16, 0), a1);

		lezioni.add(l1);
		lezioni.add(l2);
	}
}