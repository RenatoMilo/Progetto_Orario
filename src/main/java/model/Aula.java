package model;

import java.util.ArrayList;
import java.util.List;

public class Aula {
    private String nome;
    private List<Lezione> lezioniOspitate = new ArrayList<>();

    public Aula(String nome) {
        this.nome = nome;
    }

    public void addLezione(Lezione l) {
        this.lezioniOspitate.add(l);
    }

    public boolean isLibera(model.GiornoSettimana giorno, java.time.LocalTime inizio, java.time.LocalTime fine) {
        for (Lezione l : lezioniOspitate) {
            if (l.getGiorno() == giorno) {
                if (inizio.isBefore(l.getOraFine()) && fine.isAfter(l.getOraInizio())) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getNome() { return nome; }
}