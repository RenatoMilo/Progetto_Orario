package gui;

import controller.Controller;
import model.Lezione;
import model.Studente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardStudente extends JFrame {
    private JPanel mainPanel;
    private JLabel lblBenvenuto;
    private JTable tabellaOrario;
    private JButton btnLogout;

    public DashboardStudente() {

        setContentPane(mainPanel);
        setTitle("Cruscotto Studente - Orario Lezioni");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        Studente studenteLoggato = (Studente) Controller.getInstance().getUtenteLoggato();
        lblBenvenuto.setText("Benvenuto, " + studenteLoggato.getNome() + " " + studenteLoggato.getCognome() + " (" + studenteLoggato.getMatricola() + ")");


        String[] colonne = {"Giorno", "Ora Inizio", "Ora Fine", "Materia", "Aula"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        List<Lezione> lezioni = Controller.getInstance().getLezioniPerStudente(studenteLoggato);


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

        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Controller.getInstance().effettuaLogout();


                LoginFrame login = new LoginFrame();
                login.setVisible(true);
                dispose();
            }
        });
    }
}