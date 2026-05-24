package model;

import model.AnnoCorso;

public class Studente extends Utente {
    private String matricola;
    private AnnoCorso anno;


    public Studente(String nome, String cognome, String email, String login, String password, String matricola, AnnoCorso anno) {
        super(nome, cognome, email, login, password);
        this.matricola = matricola;
        this.anno = anno;
    }


    public String getMatricola() {
        return matricola;
    }

    public AnnoCorso getAnno() {
        return anno;
    }
}