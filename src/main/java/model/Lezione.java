package model;

import model.GiornoSettimana;
import java.time.LocalTime;

public class Lezione {
    private Insegnamento insegnamento;
    private GiornoSettimana giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Aula aula;
    private int idLezione;

    public Lezione(Insegnamento insegnamento, GiornoSettimana giorno, LocalTime oraInizio, LocalTime oraFine, Aula aula) {
        this.insegnamento = insegnamento;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.aula = aula;


        this.insegnamento.addLezione(this);
        this.aula.addLezione(this);
    }

    public int getIdLezione() { return idLezione; }
    public void setIdLezione(int idLezione) { this.idLezione = idLezione; }

    public Insegnamento getInsegnamento() { return insegnamento; }
    public GiornoSettimana getGiorno() { return giorno; }
    public LocalTime getOraInizio() { return oraInizio; }
    public LocalTime getOraFine() { return oraFine; }
    public Aula getAula() { return aula; }
    public void setGiorno(GiornoSettimana giorno) { this.giorno = giorno; }
    public void setOraInizio(LocalTime oraInizio) { this.oraInizio = oraInizio; }
    public void setOraFine(LocalTime oraFine) { this.oraFine = oraFine; }
}