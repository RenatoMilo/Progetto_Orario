package model;
import java.time.LocalTime;

public class TestModel {

	public static void main(String[] args) {
		System.out.println("--- Test Inizializzazione Modello di Dominio ---");

		// 1. Creazione degli utenti
		Responsabile resp = new Responsabile("Mario", "Rossi", "m.rossi@uni.it", "admin", "admin123");
		Docente prof = new Docente("Luigi", "Bianchi", "l.bianchi@uni.it", "lbianchi", "pass123");

		System.out.println("Creato Responsabile: " + resp.getNome() + " " + resp.getCognome());
		System.out.println("Creato Docente: " + prof.getNome() + " " + prof.getCognome());

		// 2. Creazione delle entità di base (Aule e Insegnamenti)
		Aula aulaA1 = new Aula("Aula A1");

		// Quando creo l'insegnamento, passo il docente titolare (associazione)
		Insegnamento progOggetti = new Insegnamento("Programmazione a Oggetti", 9, prof);

		System.out.println("\nInsegnamento creato: " + progOggetti.getNome() +
				" - Titolare: " + progOggetti.getDocenteTitolare().getCognome());

		// 3. Creazione di una lezione (aggiorna in automatico le liste in Aula e Insegnamento)
		LocalTime inizio = LocalTime.of(10, 0);
		LocalTime fine = LocalTime.of(12, 0);
		Lezione lezione1 = new Lezione(progOggetti, GiornoSettimana.LUNEDI, inizio, fine, aulaA1);

		System.out.println("\nLezione creata e collegata:");
		System.out.println("Materia: " + lezione1.getInsegnamento().getNome());
		System.out.println("Giorno: " + lezione1.getGiorno() + " dalle " + lezione1.getOraInizio() + " alle " + lezione1.getOraFine());
		System.out.println("Aula: " + lezione1.getAula().getNome());

		// 4. Creazione di una richiesta di spostamento
		LocalTime nuovoInizio = LocalTime.of(14, 0);
		LocalTime nuovaFine = LocalTime.of(16, 0);
		RichiestaSpostamento richiesta = new RichiestaSpostamento(lezione1, GiornoSettimana.MARTEDI, nuovoInizio, nuovaFine);

		System.out.println("\nRichiesta di spostamento creata per la lezione:");
		System.out.println("Giorno Proposto: " + richiesta.getGiornoProposto());
		System.out.println("Stato attuale: " + richiesta.getStato());

		// 5. Simulazione di valutazione da parte del responsabile
		richiesta.setStato(StatoRichiesta.APPROVATA);
		System.out.println("Nuovo stato dopo valutazione del Responsabile: " + richiesta.getStato());

	}

}
