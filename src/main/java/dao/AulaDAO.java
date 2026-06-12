package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AulaDAO {
    void inserisciAulaDB(String nome) throws SQLException;
    void leggiAuleDB(ArrayList<Integer> idAule, ArrayList<String> nomiAule) throws SQLException;
}