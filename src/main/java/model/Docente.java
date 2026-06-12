package model;
import java.util.ArrayList;
import java.util.List;

public class Docente extends Utente {
    // Implementazione dell'associazione 1 a molti verso Insegnamento
    private List<Insegnamento> insegnamentiTitolare = new ArrayList<>();

    public Docente(String nome, String cognome, String email, String login, String password) {
        super(nome, cognome, email, login, password);
    }

    public void addInsegnamento(Insegnamento insegnamento) {
        this.insegnamentiTitolare.add(insegnamento);
    }

    public List<Insegnamento> getInsegnamentiTitolare() { return insegnamentiTitolare; }

    @Override
    public String toString() {
        return "Prof. " + getNome() + " " + getCognome();
    }

}