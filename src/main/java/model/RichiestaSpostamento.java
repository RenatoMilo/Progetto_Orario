package model;
import model.GiornoSettimana;
import model.StatoRichiesta;
import java.time.LocalTime;

public class RichiestaSpostamento {
    private Lezione lezioneDaSpostare;
    private GiornoSettimana giornoProposto;
    private LocalTime oraInizioProposta;
    private LocalTime oraFineProposta;
    private StatoRichiesta stato;

    public RichiestaSpostamento(Lezione lezione, GiornoSettimana giornoProposto, LocalTime oraInizioProposta, LocalTime oraFineProposta) {
        this.lezioneDaSpostare = lezione;
        this.giornoProposto = giornoProposto;
        this.oraInizioProposta = oraInizioProposta;
        this.oraFineProposta = oraFineProposta;
        this.stato = StatoRichiesta.IN_ATTESA; // Valore di default alla creazione
    }

    public Lezione getLezioneDaSpostare() { return lezioneDaSpostare; }
    public GiornoSettimana getGiornoProposto() { return giornoProposto; }
    public LocalTime getOraInizioProposta() { return oraInizioProposta; }
    public LocalTime getOraFineProposta() { return oraFineProposta; }
    public StatoRichiesta getStato() { return stato; }
    public void setStato(StatoRichiesta stato) { this.stato = stato; }
}