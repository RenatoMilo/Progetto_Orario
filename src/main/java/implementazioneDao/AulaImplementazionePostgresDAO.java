package implementazioneDao;

import dao.AulaDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.ArrayList;

public class AulaImplementazionePostgresDAO implements AulaDAO {
    private Connection connection;

    public AulaImplementazionePostgresDAO() {
        try {
            this.connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inserisciAulaDB(String nome) throws SQLException {
        String query = "INSERT INTO public.\"Aula\" (\"Nome\") VALUES (?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        }
    }

    @Override
    public void leggiAuleDB(ArrayList<Integer> idAule, ArrayList<String> nomiAule) throws SQLException {
        String query = "SELECT * FROM public.\"Aula\";";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idAule.add(rs.getInt("ID_Aula"));
                nomiAule.add(rs.getString("Nome"));
            }
        }
    }
}