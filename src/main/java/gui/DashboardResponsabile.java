package gui;

import controller.Controller;
import model.Responsabile;
import model.RichiestaSpostamento;
import model.StatoRichiesta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardResponsabile extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JTable tabellaRichieste;
    private JButton btnApprova;
    private JButton btnRifiuta;
    private JButton btnLogout;

    private List<RichiestaSpostamento> richiesteAttive;
    private DefaultTableModel tableModel;

    public DashboardResponsabile() {

        setContentPane(mainPanel);
        setTitle("Area Coordinatore / Responsabile Orari");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        Responsabile respLoggato = (Responsabile) Controller.getInstance().getUtenteLoggato();
        lblBenvenuto.setText("Pannello Coordinatore: Prof. " + respLoggato.getNome() + " " + respLoggato.getCognome());


        String[] colonne = {"Materia", "Docente Richiedente", "Giorno Prop.", "Ora Inizio Prop.", "Ora Fine Prop."};
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabellaRichieste.setModel(tableModel);


        aggiornaTabella();


        btnApprova.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int riga = tabellaRichieste.getSelectedRow();
                if (riga == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona una richiesta da approvare!");
                    return;
                }
                RichiestaSpostamento r = richiesteAttive.get(riga);
                Controller.getInstance().valutaRichiesta(r, StatoRichiesta.APPROVATA);
                JOptionPane.showMessageDialog(null, "Richiesta approvata! L'orario delle lezioni è stato aggiornato.");
                aggiornaTabella();
            }
        });


        btnRifiuta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int riga = tabellaRichieste.getSelectedRow();
                if (riga == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona una richiesta da rifiutare!");
                    return;
                }
                RichiestaSpostamento r = richiesteAttive.get(riga);
                Controller.getInstance().valutaRichiesta(r, StatoRichiesta.RIFIUTATA);
                JOptionPane.showMessageDialog(null, "Richiesta rifiutata.");
                aggiornaTabella();
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

    private void aggiornaTabella() {
        tableModel.setRowCount(0);
        richiesteAttive = Controller.getInstance().getRichiesteInAttesa();
        for (RichiestaSpostamento r : richiesteAttive) {
            tableModel.addRow(new Object[]{
                    r.getLezioneDaSpostare().getInsegnamento().getNome(),
                    r.getLezioneDaSpostare().getInsegnamento().getDocenteTitolare().getCognome(),
                    r.getGiornoProposto(),
                    r.getOraInizioProposta(),
                    r.getOraFineProposta()
            });
        }
    }
}