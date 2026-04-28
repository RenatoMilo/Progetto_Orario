package model;
import java.util.ArrayList;
import java.util.List;

public class Insegnamento {
    private String nome;
    private int cfu;
    private Docente docenteTitolare;
    // Implementazione della composizione: un insegnamento comprende molte lezioni
    private List<Lezione> lezioni = new ArrayList<>();

    public Insegnamento(String nome, int cfu, Docente docenteTitolare) {
        this.nome = nome;
        this.cfu = cfu;
        this.docenteTitolare = docenteTitolare;
        this.docenteTitolare.addInsegnamento(this); // Mantiene la coerenza bidirezionale
    }

    public void addLezione(Lezione lezione) {
        this.lezioni.add(lezione);
    }

    public String getNome() { return nome; }
    public Docente getDocenteTitolare() { return docenteTitolare; }
}