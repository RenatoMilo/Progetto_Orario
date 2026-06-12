package implementazioneDao;

import dao.LezioneDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.ArrayList;

public class LezioneImplementazionePostgresDAO implements LezioneDAO {
    private Connection connection;

    public LezioneImplementazionePostgresDAO() {
        try {
            this.connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inserisciLezioneDB(int idInsegnamento, String giorno, String oraInizio, String oraFine, int idAula) throws SQLException {
        String query = "INSERT INTO public.\"Lezione\" (\"FK_Insegnamento\", \"Giorno\", \"OraInizio\", \"OraFine\", \"FK_Aula\") VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idInsegnamento);
            ps.setString(2, giorno);
            ps.setTime(3, Time.valueOf(oraInizio + ":00"));
            ps.setTime(4, Time.valueOf(oraFine + ":00"));
            ps.setInt(5, idAula);
            ps.executeUpdate();
        }
    }

    @Override
    public void leggiLezioniDB(ArrayList<Integer> idLezioni, ArrayList<Integer> idInsegnamenti, ArrayList<String> giorni, ArrayList<String> oreInizio, ArrayList<String> oreFine, ArrayList<Integer> idAule) throws SQLException {
        String query = "SELECT * FROM public.\"Lezione\";";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idLezioni.add(rs.getInt("ID_Lezione"));
                idInsegnamenti.add(rs.getInt("FK_Insegnamento"));
                giorni.add(rs.getString("Giorno"));

                oreInizio.add(rs.getTime("OraInizio").toString().substring(0, 5));
                oreFine.add(rs.getTime("OraFine").toString().substring(0, 5));

                idAule.add(rs.getInt("FK_Aula"));
            }
        }
    }

    @Override
    public void aggiornaOrarioLezioneDB(int idLezione, String giorno, String oraInizio, String oraFine) throws SQLException {
        String query = "UPDATE public.\"Lezione\" SET \"Giorno\" = ?, \"OraInizio\" = ?, \"OraFine\" = ? WHERE \"ID_Lezione\" = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, giorno);
            ps.setTime(2, Time.valueOf(oraInizio + ":00"));
            ps.setTime(3, Time.valueOf(oraFine + ":00"));
            ps.setInt(4, idLezione);
            ps.executeUpdate();
        }
    }
}