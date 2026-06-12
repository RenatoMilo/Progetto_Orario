package gui;

import controller.Controller;
import model.Aula;
import model.Docente;
import model.Insegnamento;
import model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InserimentoLezioneFrame extends JFrame {
    private JPanel mainPanel;
    private JComboBox<Insegnamento> cbInsegnamento;
    private JComboBox<GiornoSettimana> cbGiorno;
    private JTextField txtOraInizio;
    private JTextField txtOraFine;
    private JComboBox<Aula> cbAula;
    private JButton btnSalva;
    private JButton btnAnnulla;
    private JButton btnNuovoInsegnamento; // Bottone "+" Insegnamento
    private JButton btnNuovaAula;        // Bottone "+" Aula
    private JFrame frameChiamante;

    public InserimentoLezioneFrame(JFrame frameChiamante) {
        this.frameChiamante = frameChiamante;

        setContentPane(mainPanel);
        setTitle("Pianifica Nuova Lezione");
        setSize(500, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frameChiamante);

        // Popolamento iniziale dei menu a tendina
        ricaricaInsegnamenti();
        ricaricaAule();

        for (GiornoSettimana g : GiornoSettimana.values()) {
            cbGiorno.addItem(g);
        }

        // --- GESTIONE DEI PULSANTI "+" (Slide 26) ---

        // Inserimento Aula "on the fly"
        btnNuovaAula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeAula = JOptionPane.showInputDialog(null, "Inserisci il nome della nuova Aula (es: Aula A3):");
                if (nomeAula != null && !nomeAula.trim().isEmpty()) {
                    boolean ok = Controller.getInstance().inserisciNuovaAula(nomeAula.trim());
                    if (ok) {
                        JOptionPane.showMessageDialog(null, "Aula registrata con successo!");
                        ricaricaAule(); // Aggiorna il ComboBox all'istante
                    } else {
                        JOptionPane.showMessageDialog(null, "Errore: Aula già esistente.");
                    }
                }
            }
        });

        // Inserimento Insegnamento "on the fly"
        btnNuovoInsegnamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeInsegnamento = JOptionPane.showInputDialog(null, "Inserisci il nome del nuovo Insegnamento:");
                if (nomeInsegnamento == null || nomeInsegnamento.trim().isEmpty()) return;

                String cfuStr = JOptionPane.showInputDialog(null, "Inserisci i CFU:");
                if (cfuStr == null || cfuStr.trim().isEmpty()) return;
                int cfu;
                try {
                    cfu = Integer.parseInt(cfuStr.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "I CFU devono essere un numero intero!");
                    return;
                }

                // Finestra modale con scelta a tendina per l'Anno Corso (Slide 26)
                AnnoCorso anno = (AnnoCorso) JOptionPane.showInputDialog(
                        null, "Seleziona l'anno di corso:", "Anno Corso",
                        JOptionPane.QUESTION_MESSAGE, null, AnnoCorso.values(), AnnoCorso.I_ANNO
                );
                if (anno == null) return;

                java.util.List<Docente> docentiDisponibili = Controller.getInstance().getDocenti();
                if (docentiDisponibili.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Errore: Non ci sono docenti nel sistema.");
                    return;
                }

                // Finestra modale con scelta a tendina per il Docente titolare
                Object[] docentiArr = docentiDisponibili.toArray();
                Docente docenteSel = (Docente) JOptionPane.showInputDialog(
                        null, "Seleziona il docente titolare:", "Docente Titolare",
                        JOptionPane.QUESTION_MESSAGE, null, docentiArr, docentiArr[0]
                );
                if (docenteSel == null) return;

                // Invio al Controller per salvare su DB
                boolean ok = Controller.getInstance().inserisciNuovoInsegnamento(nomeInsegnamento.trim(), cfu, anno, docenteSel);
                if (ok) {
                    JOptionPane.showMessageDialog(null, "Insegnamento registrato con successo!");
                    ricaricaInsegnamenti(); // Aggiorna il ComboBox all'istante
                } else {
                    JOptionPane.showMessageDialog(null, "Errore durante l'inserimento.");
                }
            }
        });

        // Bottone Salva Lezione
        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Insegnamento insegnamento = (Insegnamento) cbInsegnamento.getSelectedItem();
                GiornoSettimana giorno = (GiornoSettimana) cbGiorno.getSelectedItem();
                String oraInizio = txtOraInizio.getText();
                String oraFine = txtOraFine.getText();
                Aula aula = (Aula) cbAula.getSelectedItem();

                if (oraInizio.isEmpty() || oraFine.isEmpty() || insegnamento == null || aula == null) {
                    JOptionPane.showMessageDialog(null, "Compila tutti i campi obbligatori!");
                    return;
                }

                boolean successo = Controller.getInstance().inserisciNuovaLezione(insegnamento, giorno, oraInizio, oraFine, aula);
                if (successo) {
                    JOptionPane.showMessageDialog(null, "Lezione pianificata con successo nel Database!");
                    tornaIndietro();
                } else {
                    JOptionPane.showMessageDialog(null, "Errore: Impossibile completare la pianificazione.");
                }
            }
        });

        btnAnnulla.addActionListener(e -> tornaIndietro());
    }

    private void ricaricaInsegnamenti() {
        cbInsegnamento.removeAllItems();
        for (Insegnamento ins : Controller.getInstance().getInsegnamenti()) {
            cbInsegnamento.addItem(ins);
        }
    }

    private void ricaricaAule() {
        cbAula.removeAllItems();
        for (Aula a : Controller.getInstance().getAule()) {
            cbAula.addItem(a);
        }
    }

    private void tornaIndietro() {
        frameChiamante.setVisible(true);
        dispose();
    }
}