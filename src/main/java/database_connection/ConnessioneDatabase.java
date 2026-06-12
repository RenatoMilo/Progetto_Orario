package database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {

    private static ConnessioneDatabase instance;
    private Connection connection = null;

    // Parametri di configurazione (modificabili a seconda del tuo database locale)
    private String nome = "postgres";
    private String password = "unina"; // Inserisci qui la password di pgAdmin 4
    private String url = "jdbc:postgresql://localhost:5432/OrarioLezioniDB";
    private String driver = "org.postgresql.Driver";

    // Costruttore privato (Singleton)
    private ConnessioneDatabase() throws SQLException {
        try {
            Class.forName(driver); // Caricamento dinamico del driver (Slide 9)
            this.connection = DriverManager.getConnection(url, nome, password);
        } catch (ClassNotFoundException ex) {
            System.err.println("Errore: Driver PostgreSQL non trovato nel progetto.");
            ex.printStackTrace();
        }
    }

    // Metodo per ottenere la connessione (Slide 10)
    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConnessioneDatabase();
        } else if (instance.getConnection().isClosed()) {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}