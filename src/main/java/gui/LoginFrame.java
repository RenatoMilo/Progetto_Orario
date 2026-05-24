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

    public LoginFrame() {
        setContentPane(mainPanel);
        setTitle("Sistema Orario Lezioni - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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
                    }
                    else if (loggato instanceof Responsabile) {
                        DashboardResponsabile dashboardResponsabile = new DashboardResponsabile();
                        dashboardResponsabile.setVisible(true);
                        dispose();
                    }


                    else if (loggato instanceof Docente) {
                        DashboardDocente dashboardDocente = new DashboardDocente();
                        dashboardDocente.setVisible(true);
                        dispose();
                    }


                } else {
                    lblErrore.setText("Credenziali errate! Riprova.");
                }
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