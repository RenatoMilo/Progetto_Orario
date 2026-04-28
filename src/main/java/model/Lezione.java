package model;
import model.GiornoSettimana;
import java.time.LocalTime;

public class Lezione {
    private GiornoSettimana giorno;
    private LocalTime oraInizio;
    private LocalTime oraFine;
    private Aula aula;
    private Insegnamento insegnamento;

    public Lezione(Insegnamento insegnamento, GiornoSettimana giorno, LocalTime oraInizio, LocalTime oraFine, Aula aula) {
        this.insegnamento = insegnamento;
        this.giorno = giorno;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.aula = aula;

        // Colleghiamo automaticamente la lezione all'aula e all'insegnamento
        this.aula.addLezione(this);
        this.insegnamento.addLezione(this);
    }

    public GiornoSettimana getGiorno() { return giorno; }
    public LocalTime getOraInizio() { return oraInizio; }
    public LocalTime getOraFine() { return oraFine; }
    public Aula getAula() { return aula; }
    public Insegnamento getInsegnamento() { return insegnamento; }
}