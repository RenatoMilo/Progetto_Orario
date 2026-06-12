package gui;

import controller.Controller;
import model.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrazioneFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtEmail;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRuolo;
    private JComboBox<AnnoCorso> cbAnno;
    private JButton btnSalva;
    private JButton btnAnnulla;
    private JFrame frameChiamante;

    public RegistrazioneFrame(JFrame frameChiamante) {
        this.frameChiamante = frameChiamante;

        setContentPane(mainPanel);
        setTitle("Registrazione Nuovo Utente");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(frameChiamante);

        // Popoliamo i JComboBox
        cbRuolo.addItem("STUDENTE");
        cbRuolo.addItem("DOCENTE");
        cbRuolo.addItem("RESPONSABILE");

        for (AnnoCorso a : AnnoCorso.values()) {
            cbAnno.addItem(a);
        }

        // Di default abilitiamo il campo anno corso per gli studenti
        cbAnno.setEnabled(true);

        // Listener per abilitare/disabilitare anno corso a seconda del ruolo
        cbRuolo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isStudente = cbRuolo.getSelectedItem().toString().equals("STUDENTE");
                cbAnno.setEnabled(isStudente);
            }
        });

        // Gestione dell'azione sul bottone Salva
        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String cognome = txtCognome.getText();
                String email = txtEmail.getText();
                String login = txtLogin.getText();
                String pwd = new String(txtPassword.getPassword());
                String ruolo = cbRuolo.getSelectedItem().toString();
                String anno = cbAnno.getSelectedItem().toString();

                if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || login.isEmpty() || pwd.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Compila tutti i campi obbligatori!");
                    return;
                }

                // Chiamo il Controller per inserire nel DB (matricola viene omessa) (Slide 15)
                boolean successo = Controller.getInstance().registraNuovoUtente(nome, cognome, email, login, pwd, ruolo, anno);
                if (successo) {
                    JOptionPane.showMessageDialog(null, "Nuovo utente registrato con successo nel Database!");
                    tornaIndietro();
                } else {
                    JOptionPane.showMessageDialog(null, "Errore: Impossibile completare la registrazione.");
                }
            }
        });

        btnAnnulla.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tornaIndietro();
            }
        });
    }

    private void tornaIndietro() {
        frameChiamante.setVisible(true);
        dispose();
    }
}