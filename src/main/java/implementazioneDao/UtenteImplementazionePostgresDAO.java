package implementazioneDao;

import dao.UtenteDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.ArrayList;

public class UtenteImplementazionePostgresDAO implements UtenteDAO {
    private Connection connection;

    public UtenteImplementazionePostgresDAO() {
        try {
            this.connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String loginDB(String login, String password) throws SQLException {
        String query = "SELECT u.\"ID_Utente\", d.\"IsResponsabile\", s.\"ID_Utente\" AS \"IsStudente\" " +
                "FROM public.\"Utente\" u " +
                "LEFT JOIN public.\"Docente\" d ON u.\"ID_Utente\" = d.\"ID_Utente\" " +
                "LEFT JOIN public.\"Studente\" s ON u.\"ID_Utente\" = s.\"ID_Utente\" " +
                "WHERE u.\"Login\" = ? AND u.\"Password\" = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getObject("IsStudente") != null) {
                        return "STUDENTE";
                    } else if (rs.getBoolean("IsResponsabile")) {
                        return "RESPONSABILE";
                    } else {
                        return "DOCENTE";
                    }
                }
            }
        }
        return null; // Credenziali non corrispondenti
    }

    @Override
    public void leggiDatiUtenteLoggatoDB(String login, ArrayList<Integer> idUtente, ArrayList<String> nome, ArrayList<String> cognome, ArrayList<String> email, ArrayList<String> matricola, ArrayList<String> annoCorso, ArrayList<Boolean> isResponsabile) throws SQLException {
        String query = "SELECT u.*, s.\"Matricola\", s.\"AnnoCorso\", d.\"IsResponsabile\" " +
                "FROM public.\"Utente\" u " +
                "LEFT JOIN public.\"Studente\" s ON u.\"ID_Utente\" = s.\"ID_Utente\" " +
                "LEFT JOIN public.\"Docente\" d ON u.\"ID_Utente\" = d.\"ID_Utente\" " +
                "WHERE u.\"Login\" = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idUtente.add(rs.getInt("ID_Utente"));
                    nome.add(rs.getString("Nome"));
                    cognome.add(rs.getString("Cognome"));
                    email.add(rs.getString("Email"));

                    String matr = rs.getString("Matricola");
                    matricola.add(matr != null ? matr : "");

                    String anno = rs.getString("AnnoCorso");
                    annoCorso.add(anno != null ? anno : "");

                    isResponsabile.add(rs.getBoolean("IsResponsabile"));
                }
            }
        }
    }

    @Override
    public void leggiTuttiIDocentiDB(ArrayList<Integer> idDocenti, ArrayList<String> nomi, ArrayList<String> cognomi, ArrayList<String> email, ArrayList<String> logins, ArrayList<String> password, ArrayList<Boolean> isResponsabile) throws SQLException {
        String query = "SELECT u.*, d.\"IsResponsabile\" FROM public.\"Utente\" u JOIN public.\"Docente\" d ON u.\"ID_Utente\" = d.\"ID_Utente\";";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idDocenti.add(rs.getInt("ID_Utente"));
                nomi.add(rs.getString("Nome"));
                cognomi.add(rs.getString("Cognome"));
                email.add(rs.getString("Email"));
                logins.add(rs.getString("Login"));
                password.add(rs.getString("Password"));
                isResponsabile.add(rs.getBoolean("IsResponsabile"));
            }
        }
    }

    // UNICO METODO DI REGISTRAZIONE (Senza il parametro matricola, allineato a UtenteDAO)
    @Override
    public void registraUtenteDB(String nome, String cognome, String email, String login, String password, String ruolo, String annoCorso) throws SQLException {
        String queryUtente = "INSERT INTO public.\"Utente\" (\"Nome\", \"Cognome\", \"Email\", \"Login\", \"Password\") VALUES (?, ?, ?, ?, ?) RETURNING \"ID_Utente\";";

        try (PreparedStatement psUtente = connection.prepareStatement(queryUtente)) {
            psUtente.setString(1, nome);
            psUtente.setString(2, cognome);
            psUtente.setString(3, email);
            psUtente.setString(4, login);
            psUtente.setString(5, password);

            try (ResultSet rs = psUtente.executeQuery()) {
                if (rs.next()) {
                    int idUtente = rs.getInt("ID_Utente");

                    if (ruolo.equalsIgnoreCase("STUDENTE")) {
                        // Omettiamo la colonna Matricola: Postgres userà il DEFAULT generato dalla SEQUENCE
                        String queryStudente = "INSERT INTO public.\"Studente\" (\"ID_Utente\", \"AnnoCorso\") VALUES (?, ?);";
                        try (PreparedStatement psStudente = connection.prepareStatement(queryStudente)) {
                            psStudente.setInt(1, idUtente);
                            psStudente.setString(2, annoCorso);
                            psStudente.executeUpdate();
                        }
                    } else {
                        boolean isResp = ruolo.equalsIgnoreCase("RESPONSABILE");
                        String queryDocente = "INSERT INTO public.\"Docente\" (\"ID_Utente\", \"IsResponsabile\") VALUES (?, ?);";
                        try (PreparedStatement psDocente = connection.prepareStatement(queryDocente)) {
                            psDocente.setInt(1, idUtente);
                            psDocente.setBoolean(2, isResp);
                            psDocente.executeUpdate();
                        }
                    }
                }
            }
        }
    }
}