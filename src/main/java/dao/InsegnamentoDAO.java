package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface InsegnamentoDAO {
    void inserisciInsegnamentoDB(String nome, int cfu, String annoCorso, int idDocenteTitolare) throws SQLException;
    void leggiInsegnamentiDB(ArrayList<Integer> idInsegnamenti, ArrayList<String> nomi, ArrayList<Integer> cfuList, ArrayList<String> anniCorso, ArrayList<Integer> idDocenti) throws SQLException;
}