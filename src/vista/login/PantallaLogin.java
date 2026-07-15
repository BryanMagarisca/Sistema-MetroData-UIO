package vista.login;

import persistencia.UsuarioRepository;
import modelo.Pasajero;
import modelo.Usuario;
import persistencia.PasajeroRepository;
import utilidades.Estilos;
import utilidades.SesionUsuario;
import vista.MainFrame;
import javax.swing.*;
import java.awt.*;

public class PantallaLogin extends JPanel {

    private MainFrame mainFrame;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    public PantallaLogin(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(Estilos.ROJO_METRO); // Fondo institucional Rojo
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(400, 520));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblLogin = new JLabel("INICIAR SESIÓN", SwingConstants.CENTER);
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLogin.setForeground(Estilos.ROJO_METRO); // Título en Rojo para consistencia
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 40, 0);
        card.add(lblLogin, gbc);

        gbc.insets = new Insets(0, 0, 5, 0);
        gbc.gridy = 1; card.add(new JLabel("Usuario / Cédula:"), gbc);
        txtUsuario = new JTextField();
        txtUsuario.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 20, 0);
        card.add(txtUsuario, gbc);

        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 5, 0);
        card.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 40, 0);
        card.add(txtPassword, gbc);

        // BOTÓN ROJO CON TEXTO BLANCO (MÁXIMA CONSISTENCIA)
        JButton btnEntrar = new JButton("ACCEDER AL SISTEMA");
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setBackground(Estilos.ROJO_METRO); 
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setPreferredSize(new Dimension(0, 50));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setOpaque(true);
        btnEntrar.setBorderPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(e -> autenticar());
        gbc.gridy = 5; card.add(btnEntrar, gbc);

        JButton btnVolver = new JButton("Volver al Inicio");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVolver.setForeground(Estilos.GRIS_TEXTO);
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            mainFrame.mostrarPanel("INSTITUCIONAL");
        });
        gbc.gridy = 6; gbc.insets = new Insets(20, 0, 0, 0);
        card.add(btnVolver, gbc);

        add(card);
    }

    private void autenticar() {
        String user = txtUsuario.getText();
        String pass = new String(txtPassword.getPassword());

        Usuario u = UsuarioRepository.getInstancia().autenticar(user, pass);
        if (u != null) {
            SesionUsuario.usuarioLogueado = u;
            limpiarCampos();
            mainFrame.mostrarPanel("MENU_ADMIN");
            return;
        }

        Pasajero dummy = new Pasajero(user, "", "", "", "", null, "", pass);
        Pasajero p = PasajeroRepository.getInstancia().getArbolPasajeros().buscar(dummy);
        
        if (p != null && p.getPassword().equals(pass)) {
            SesionUsuario.usuarioLogueado = p;
            limpiarCampos();
            mainFrame.mostrarPanel("MENU_USUARIO");
            return;
        }

        JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
    }
}