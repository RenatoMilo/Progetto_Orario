package implementazioneDao;


import dao.RichiestaSpostamentoDAO;
import database_connection.ConnessioneDatabase;
import java.sql.*;
import java.util.ArrayList;

public class RichiestaSpostamentoImplementazioneDAO implements RichiestaSpostamentoDAO {
    private Connection connection;

    public RichiestaSpostamentoImplementazioneDAO() {
        try {
            this.connection = ConnessioneDatabase.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void inviaRichiestaDB(int idLezione, String giornoProposto, String oraInizioProposta, String oraFineProposta) throws SQLException {
        String query = "INSERT INTO public.\"RichiestaSpostamento\" (\"FK_Lezione\", \"GiornoProposto\", \"OraInizioProposta\", \"OraFineProposta\") VALUES (?, ?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, idLezione);
            ps.setString(2, giornoProposto);
            ps.setTime(3, Time.valueOf(oraInizioProposta + ":00"));
            ps.setTime(4, Time.valueOf(oraFineProposta + ":00"));
            ps.executeUpdate();
        }
    }

    @Override
    public void valutaRichiestaDB(int idRichiesta, String nuovoStato) throws SQLException {
        String query = "UPDATE public.\"RichiestaSpostamento\" SET \"Stato\" = ? WHERE \"ID_Richiesta\" = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nuovoStato);
            ps.setInt(2, idRichiesta);
            ps.executeUpdate();
        }
    }

    @Override
    public void leggiRichiesteDB(ArrayList<Integer> idRichieste, ArrayList<Integer> idLezioni, ArrayList<String> giorniProp, ArrayList<String> oreInizioProp, ArrayList<String> oreFineProp, ArrayList<String> stati) throws SQLException {
        String query = "SELECT * FROM public.\"RichiestaSpostamento\";";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                idRichieste.add(rs.getInt("ID_Richiesta"));
                idLezioni.add(rs.getInt("FK_Lezione"));
                giorniProp.add(rs.getString("GiornoProposto"));

                oreInizioProp.add(rs.getTime("OraInizioProposta").toString().substring(0, 5));
                oreFineProp.add(rs.getTime("OraFineProposta").toString().substring(0, 5));

                stati.add(rs.getString("Stato"));
            }
        }
    }
}