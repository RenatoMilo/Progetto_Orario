package model;

import model.AnnoCorso;
import java.util.ArrayList;
import java.util.List;

public class Insegnamento {
    private String nome;
    private int cfu;
    private AnnoCorso annoDiCorso;
    private Docente docenteTitolare;
    private List<Lezione> lezioni = new ArrayList<>();
    private int idInsegnamento;


    public Insegnamento(String nome, int cfu, AnnoCorso annoDiCorso, Docente docenteTitolare) {
        this.nome = nome;
        this.cfu = cfu;
        this.annoDiCorso = annoDiCorso;
        this.docenteTitolare = docenteTitolare;
        this.docenteTitolare.addInsegnamento(this);
    }

    public void addLezione(Lezione lezione) {
        this.lezioni.add(lezione);
    }

    public int getIdInsegnamento() { return idInsegnamento; }
    public void setIdInsegnamento(int idInsegnamento) { this.idInsegnamento = idInsegnamento; }


    public String getNome() { return nome; }
    public Docente getDocenteTitolare() { return docenteTitolare; }
    public AnnoCorso getAnnoDiCorso() { return annoDiCorso; }
    @Override
    public String toString() {
        return nome + " (" + annoDiCorso + ")";
    }

}