package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UtenteDAO {
    // Rimosso il parametro String matricola
    void registraUtenteDB(String nome, String cognome, String email, String login, String password, String ruolo, String annoCorso) throws SQLException;

    String loginDB(String login, String password) throws SQLException;
    void leggiDatiUtenteLoggatoDB(String login, ArrayList<Integer> idUtente, ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email, ArrayList<String> matricola, ArrayList<String> annoCorso, ArrayList<Boolean> isResponsabile) throws SQLException;
    void leggiTuttiIDocentiDB(ArrayList<Integer> idDocenti, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> email, ArrayList<String> logins, ArrayList<String> password, ArrayList<Boolean> isResponsabile) throws SQLException;
}