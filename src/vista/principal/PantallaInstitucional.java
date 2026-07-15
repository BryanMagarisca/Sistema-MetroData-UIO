package vista.principal;

import utilidades.Estilos;
import vista.MainFrame;
import javax.swing.*;
import java.awt.*;

public class PantallaInstitucional extends JPanel {

    private MainFrame mainFrame;

    public PantallaInstitucional(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBackground(Estilos.ROJO_METRO);
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;

        // 1. Título Principal
        JLabel lblBienvenida = new JLabel("SISTEMA METRODATA UIO");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 72));
        lblBienvenida.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(lblBienvenida, gbc);

        // 2. Slogan
        JLabel lblSlogan = new JLabel("Sistema Integral de Gestión de Transporte Metropolitano");
        lblSlogan.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        lblSlogan.setForeground(new Color(255, 255, 255, 220));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 60, 0);
        add(lblSlogan, gbc);

        // 3. Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        panelBotones.setOpaque(false);

        // Botón Ingresar: Blanco con texto Rojo Metro para alto contraste
        JButton btnLogin = new JButton("INGRESAR AL SISTEMA");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setForeground(Estilos.ROJO_METRO);
        btnLogin.setBackground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(300, 65));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> mainFrame.mostrarPanel("LOGIN"));

        // Botón Registro: Texto Blanco con borde para contraste sobre el fondo rojo
        JButton btnRegistro = new JButton("REGISTRARSE");
        btnRegistro.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRegistro.setForeground(Color.WHITE);
        btnRegistro.setBackground(Estilos.ROJO_METRO);
        btnRegistro.setPreferredSize(new Dimension(300, 65));
        btnRegistro.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        btnRegistro.setFocusPainted(false);
        btnRegistro.setContentAreaFilled(false); // Transparente para ver el rojo de fondo
        btnRegistro.setOpaque(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistro.addActionListener(e -> mainFrame.mostrarPanel("REGISTRO"));

        panelBotones.add(btnLogin);
        panelBotones.add(btnRegistro);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(panelBotones, gbc);

        // 4. Footer
        JLabel lblFooter = new JLabel("© 2026 Sistema MetroData UIO - Proyecto | Escuela Politécnica Nacional");
        lblFooter.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFooter.setForeground(new Color(255, 255, 255, 180));
        gbc.gridy = 3;
        gbc.insets = new Insets(100, 0, 0, 0);
        add(lblFooter, gbc);
    }
}