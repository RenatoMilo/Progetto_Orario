package model;

public abstract class Utente {
    private String nome;
    private String cognome;
    private String email;
    private String login;
    private String password;
    private int idUtente;

    public Utente(String nome, String cognome, String email, String login, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public String getNome() { return nome; }
    public String getCognome() { return cognome; }

    public String getLogin() { return login; }

    public String getPassword() { return password; }

    public int getIdUtente() { return idUtente; }
    public void setIdUtente(int idUtente) { this.idUtente = idUtente; }

}
