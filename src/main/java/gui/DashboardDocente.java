package gui;

import controller.Controller;
import model.Docente;
import model.Lezione;
import model.GiornoSettimana;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardDocente extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JTable tabellaOrario;
    private JComboBox<GiornoSettimana> cbGiorno;
    private JTextField txtOraInizio;
    private JTextField txtOraFine;
    private JButton btnInviaRichiesta;
    private JButton btnLogout;

    public DashboardDocente() {
        setContentPane(mainPanel);
        setTitle("Cruscotto Docente");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        Docente docenteLoggato = (Docente) Controller.getInstance().getUtenteLoggato();
        lblBenvenuto.setText("Prof. " + docenteLoggato.getNome() + " " + docenteLoggato.getCognome());


        for (GiornoSettimana g : GiornoSettimana.values()) {
            cbGiorno.addItem(g);
        }


        String[] colonne = {"Giorno", "Ora Inizio", "Ora Fine", "Materia", "Aula"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Lezione> lezioni = Controller.getInstance().getLezioniPerDocente(docenteLoggato);
        for (Lezione l : lezioni) {
            model.addRow(new Object[]{
                    l.getGiorno(),
                    l.getOraInizio(),
                    l.getOraFine(),
                    l.getInsegnamento().getNome(),
                    l.getAula().getNome()
            });
        }
        tabellaOrario.setModel(model);


        btnInviaRichiesta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int rigaSelezionata = tabellaOrario.getSelectedRow();
                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona prima una lezione dalla tabella!");
                    return;
                }


                Lezione lezioneSelezionata = lezioni.get(rigaSelezionata);
                GiornoSettimana giornoProp = (GiornoSettimana) cbGiorno.getSelectedItem();
                String oraInizioProp = txtOraInizio.getText();
                String oraFineProp = txtOraFine.getText();

                if (oraInizioProp.isEmpty() || oraFineProp.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci gli orari proposti!");
                    return;
                }


                Controller.getInstance().inviaRichiestaSpostamento(lezioneSelezionata, giornoProp, oraInizioProp, oraFineProp);
                JOptionPane.showMessageDialog(null, "Richiesta inviata al responsabile!");

                // Puliamo i campi di testo
                txtOraInizio.setText("");
                txtOraFine.setText("");
            }
        });


        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.getInstance().effettuaLogout();
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }
}