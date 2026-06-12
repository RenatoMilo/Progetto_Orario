package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface LezioneDAO {
    void inserisciLezioneDB(int idInsegnamento, String giorno, String oraInizio, String oraFine, int idAula) throws SQLException;

    void leggiLezioniDB(ArrayList<Integer> idLezioni, ArrayList<Integer> idInsegnamenti, ArrayList<String> giorni, ArrayList<String> oreInizio, ArrayList<String> oreFine, ArrayList<Integer> idAule) throws SQLException;

    // Assicurati che ci sia questa riga con le stesse lettere maiuscole/minuscole
    void aggiornaOrarioLezioneDB(int idLezione, String giorno, String oraInizio, String oraFine) throws SQLException;
}