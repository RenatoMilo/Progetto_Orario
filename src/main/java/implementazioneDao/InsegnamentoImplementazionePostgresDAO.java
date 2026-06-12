package implementazioneDao;

import dao.InsegnamentoDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.ArrayList;

public class InsegnamentoImplementazionePostgresDAO implements InsegnamentoDAO {
    private Connection connection;

    public InsegnamentoImplementazionePostgresDAO() {
        try {
            this.connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inserisciInsegnamentoDB(String nome, int cfu, String annoCorso, int idDocenteTitolare) throws SQLException {
        String query = "INSERT INTO public.\"Insegnamento\" (\"Nome\", \"CFU\", \"AnnoCorso\", \"FK_Docente\") VALUES (?, ?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nome);
            ps.setInt(2, cfu);
            ps.setString(3, annoCorso);
            if (idDocenteTitolare == -1) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, idDocenteTitolare);
            }
            ps.executeUpdate();
        }
    }

    @Override
    public void leggiInsegnamentiDB(ArrayList<Integer> idInsegnamenti, ArrayList<String> nomi, ArrayList<Integer> cfuList, ArrayList<String> anniCorso, ArrayList<Integer> idDocenti) throws SQLException {
        String query = "SELECT * FROM public.\"Insegnamento\";";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idInsegnamenti.add(rs.getInt("ID_Insegnamento"));
                nomi.add(rs.getString("Nome"));
                cfuList.add(rs.getInt("CFU"));
                anniCorso.add(rs.getString("AnnoCorso"));
                idDocenti.add(rs.getInt("FK_Docente"));
            }
        }
    }
}