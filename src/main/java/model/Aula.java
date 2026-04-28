package model;
import java.util.ArrayList;
import java.util.List;

public class Aula {
    private String nome;
    // Implementazione dell'aggregazione: un'aula ospita molte lezioni
    private List<Lezione> lezioniOspitate = new ArrayList<>();

    public Aula(String nome) {
        this.nome = nome;
    }

    public void addLezione(Lezione lezione) {
        this.lezioniOspitate.add(lezione);
    }

    public String getNome() { return nome; }
}
