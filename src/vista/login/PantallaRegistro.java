package vista.login;

import controlador.PasajeroController;
import utilidades.Estilos;
import vista.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PantallaRegistro extends JPanel {

    private MainFrame mainFrame;
    private PasajeroController controller;
    private JTextField txtCedula, txtNombre, txtApellido, txtTelefono, txtDomicilio, txtFecha, txtCorreo;
    private JPasswordField txtPassword;

    public PantallaRegistro(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.controller = new PasajeroController();
        setLayout(new GridBagLayout());
        setBackground(Estilos.ROJO_METRO);
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Tarjeta Central más ancha para campos más largos
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(600, 700)); // Aumentado de 450 a 600
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTit = new JLabel("REGISTRO DE PASAJERO", SwingConstants.CENTER);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTit.setForeground(Estilos.AZUL_OSCURO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        card.add(lblTit, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);
        txtCedula = agregarCampo(card, "Cédula (10 dígitos):", 1, gbc);
        txtNombre = agregarCampo(card, "Nombre:", 2, gbc);
        txtApellido = agregarCampo(card, "Apellido:", 3, gbc);
        txtTelefono = agregarCampo(card, "Teléfono (10 dígitos):", 4, gbc);
        txtDomicilio = agregarCampo(card, "Domicilio:", 5, gbc);
        txtFecha = agregarCampo(card, "F. Nacimiento (aaaa-mm-dd):", 6, gbc);
        txtCorreo = agregarCampo(card, "Correo Electrónico:", 7, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        card.add(new JLabel("Contraseña:"), gbc);
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(0, 35));
        gbc.gridx = 1;
        card.add(txtPassword, gbc);

        // BOTÓN CON ALTO CONTRASTE
        JButton btnReg = new JButton("FINALIZAR REGISTRO");
        btnReg.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReg.setBackground(Estilos.AZUL_OSCURO);
        btnReg.setForeground(Color.WHITE);
        btnReg.setPreferredSize(new Dimension(0, 55));
        btnReg.setFocusPainted(false);
        btnReg.setOpaque(true);
        btnReg.setBorderPainted(false);
        btnReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReg.addActionListener(e -> registrar());
        
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 5, 10);
        card.add(btnReg, gbc);

        JButton btnVolver = new JButton("¿Ya tienes cuenta? Inicia sesión");
        btnVolver.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVolver.setForeground(Estilos.AZUL_OSCURO);
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            mainFrame.mostrarPanel("LOGIN");
        });
        gbc.gridy = 10;
        gbc.insets = new Insets(10, 10, 10, 10);
        card.add(btnVolver, gbc);

        add(card);
    }

    private JTextField agregarCampo(JPanel p, String label, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y;
        p.add(new JLabel(label), gbc);
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(300, 35)); // Ancho fijo mayor para visibilidad
        gbc.gridx = 1;
        p.add(tf, gbc);
        return tf;
    }

    private void registrar() {
        try {
            String cedula = txtCedula.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String pass = new String(txtPassword.getPassword());

            // Validaciones de longitud
            if (cedula.length() != 10 || !cedula.matches("\\d+")) {
                throw new Exception("La cédula debe tener exactamente 10 dígitos numéricos.");
            }
            if (telefono.length() != 10 || !telefono.matches("\\d+")) {
                throw new Exception("El teléfono debe tener exactamente 10 dígitos numéricos.");
            }
            if (pass.isEmpty()) {
                throw new Exception("La contraseña es obligatoria.");
            }

            // Validación de fecha
            LocalDate fNac;
            try {
                fNac = LocalDate.parse(txtFecha.getText(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception e) {
                throw new Exception("Formato de fecha inválido. Use AAAA-MM-DD");
            }

            if (fNac.isAfter(LocalDate.now())) {
                throw new Exception("La fecha de nacimiento no puede ser futura.");
            }

            int edad = Period.between(fNac, LocalDate.now()).getYears();
            if (edad < 5) {
                throw new Exception("El usuario debe tener al menos 5 años de edad.");
            }
            
            if (controller.registrarPasajeroFull(cedula, txtNombre.getText(), txtApellido.getText(), telefono, txtDomicilio.getText(), fNac, txtCorreo.getText(), pass)) {
                JOptionPane.showMessageDialog(this, "¡Registro exitoso!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                mainFrame.mostrarPanel("LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Esta cédula ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtCedula.setText(""); txtNombre.setText(""); txtApellido.setText("");
        txtTelefono.setText(""); txtDomicilio.setText("");
        txtFecha.setText(""); txtCorreo.setText(""); txtPassword.setText("");
    }
}