package gui;

import controller.Controller;
import model.Docente;
import model.Responsabile;
import model.Studente;
import model.Utente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblErrore;
    private JButton btnRegistra; // Aggiunto bottone di registrazione

    public LoginFrame() {
        setContentPane(mainPanel);
        setTitle("Sistema Orario Lezioni - Login");
        setSize(400, 350); // Leggermente aumentata l'altezza per ospitare il nuovo bottone
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Bottone Accedi
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = txtLogin.getText();
                String password = new String(txtPassword.getPassword());

                boolean successo = Controller.getInstance().effettuaLogin(login, password);

                if (successo) {
                    lblErrore.setText("");
                    Utente loggato = Controller.getInstance().getUtenteLoggato();

                    if (loggato instanceof Studente) {
                        DashboardStudente dashboard = new DashboardStudente();
                        dashboard.setVisible(true);
                        dispose();
                    } else if (loggato instanceof Responsabile) {
                        DashboardResponsabile dashboardResponsabile = new DashboardResponsabile();
                        dashboardResponsabile.setVisible(true);
                        dispose();
                    } else if (loggato instanceof Docente) {
                        DashboardDocente dashboardDocente = new DashboardDocente();
                        dashboardDocente.setVisible(true);
                        dispose();
                    }
                } else {
                    lblErrore.setText("Credenziali errate! Riprova.");
                }
            }
        });

        // Bottone Registrati (Slide 30)
        btnRegistra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Istanzio il frame passandogli questo come frame chiamante (Slide 30)
                RegistrazioneFrame regFrame = new RegistrazioneFrame(LoginFrame.this);
                regFrame.setVisible(true);
                setVisible(false); // Nascondo la schermata di login (Slide 30)
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame frame = new LoginFrame();
                frame.setVisible(true);
            }
        });
    }
}