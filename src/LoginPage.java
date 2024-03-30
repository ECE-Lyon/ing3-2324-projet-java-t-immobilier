import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        super("Login Page");

        JLabel usernameLabel = new JLabel("Identifiant:");
        JLabel passwordLabel = new JLabel("Mot de passe:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Connexion");
        JButton createAccountButton = new JButton("Créer un compte");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Etablit d'abord la connexion à la base de données
                try {
                    ConnectionDatabase.connect();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LoginPage.this, "Erreur lors de la connexion à la base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String email = usernameField.getText();
                String password = new String(passwordField.getPassword());

                boolean authenticated = ConnectionDatabase.verifyConnection(email, password);

                ConnectionDatabase.closeConnection();

                if (authenticated) {
                    JOptionPane.showMessageDialog(LoginPage.this, "Vous êtes connecté !");
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, "Identifiant ou mot de passe incorrect.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvre une nouvelle fenêtre pour créer un nouveau compte
                JFrame newAccountFrame = new JFrame("Créer un nouveau compte");
                newAccountFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                newAccountFrame.setSize(300, 150);
                newAccountFrame.setLocationRelativeTo(LoginPage.this);

                JLabel newUsernameLabel = new JLabel("Nouvel identifiant:");
                JLabel newPasswordLabel = new JLabel("Nouveau mot de passe:");
                JTextField newUsernameField = new JTextField(20);
                JPasswordField newPasswordField = new JPasswordField(20);
                JButton createButton = new JButton("Créer");

                createButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String newEmail = newUsernameField.getText();
                        String newPassword = new String(newPasswordField.getPassword());
                        try {
                            ConnectionDatabase.connect();
                            String sqlQuery = "INSERT INTO UTILISATEUR (email, password, inscription_date, name, status_user) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = ConnectionDatabase.getConnection().prepareStatement(sqlQuery);
                            statement.setString(1, newEmail);
                            statement.setString(2, newPassword);
                            statement.setDate(3, new java.sql.Date(new Date().getTime())); // Insère la date actuelle
                            statement.setString(4, newEmail); // Valeur par défaut pour le nom
                            statement.setBoolean(5, false); // Valeur par défaut pour le statut
                            int rowsInserted = statement.executeUpdate();
                            if (rowsInserted > 0) {
                                JOptionPane.showMessageDialog(newAccountFrame, "Nouveau compte créé avec succès !");
                            }
                            ConnectionDatabase.closeConnection();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(newAccountFrame, "Erreur lors de la création du compte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }

                        // Ferme la fenêtre de création de compte après création
                        newAccountFrame.dispose();
                    }
                });

                JPanel newPanel = new JPanel(new GridLayout(3, 2));
                newPanel.add(newUsernameLabel);
                newPanel.add(newUsernameField);
                newPanel.add(newPasswordLabel);
                newPanel.add(newPasswordField);
                newPanel.add(createButton);

                newAccountFrame.getContentPane().add(newPanel);
                newAccountFrame.setVisible(true);
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(createAccountButton);

        getContentPane().add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginPage();
            }
        });
    }
}
