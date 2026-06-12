package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface RichiestaSpostamentoDAO {
    void inviaRichiestaDB(int idLezione, String giornoProposto, String oraInizioProposta, String oraFineProposta) throws SQLException;
    void valutaRichiestaDB(int idRichiesta, String nuovoStato) throws SQLException;
    void leggiRichiesteDB(ArrayList<Integer> idRichieste, ArrayList<Integer> idLezioni, ArrayList<String> giorniProp, ArrayList<String> oreInizioProp, ArrayList<String> oreFineProp, ArrayList<String> stati) throws SQLException;
}