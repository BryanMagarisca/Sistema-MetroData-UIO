package vista;

import estructuras.Pila;
import utilidades.Estilos;
import utilidades.SesionUsuario;
import vista.administrador.DashboardAdmin;
import vista.usuario.DashboardUsuario;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private JPanel barraNavegacion;
    private JLabel lblModuloActual, lblReloj, lblUsuarioInfo;

    public MainFrame() {
        Estilos.aplicarEstiloGlobal();
        setTitle("METRODATA UIO - Sistema de Gestión");
        setSize(1350, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        inicializarEstructuraBase();
        iniciarServicios();
    }

    private void inicializarEstructuraBase() {
        setLayout(new BorderLayout());

        barraNavegacion = new JPanel(new BorderLayout());
        barraNavegacion.setBackground(Estilos.BLANCO);
        barraNavegacion.setPreferredSize(new Dimension(getWidth(), 65));
        barraNavegacion.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Estilos.GRIS_BORDE));

        JPanel pIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        pIzquierdo.setOpaque(false);
        JLabel lblLogo = new JLabel("METRODATA UIO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(Estilos.ROJO_METRO);
        lblModuloActual = new JLabel("|  BIENVENIDO");
        lblModuloActual.setForeground(Estilos.GRIS_TEXTO);
        pIzquierdo.add(lblLogo);
        pIzquierdo.add(lblModuloActual);

        JPanel pDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 15));
        pDerecho.setOpaque(false);
        lblUsuarioInfo = new JLabel("Invitado");
        lblUsuarioInfo.setFont(Estilos.FUENTE_NEGRILLA);
        lblReloj = new JLabel("--:--");
        lblReloj.setForeground(Estilos.GRIS_TEXTO);
        
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setForeground(Estilos.ERROR);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            SesionUsuario.limpiar();
            mostrarPanel("INSTITUCIONAL");
        });

        pDerecho.add(lblReloj);
        pDerecho.add(new JSeparator(SwingConstants.VERTICAL));
        pDerecho.add(lblUsuarioInfo);
        pDerecho.add(btnLogout);

        barraNavegacion.add(pIzquierdo, BorderLayout.WEST);
        barraNavegacion.add(pDerecho, BorderLayout.EAST);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setBackground(Estilos.GRIS_FONDO);

        add(barraNavegacion, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);
        
        barraNavegacion.setVisible(false);
    }

    public void agregarPanel(JPanel panel, String nombre) {
        panelContenedor.add(panel, nombre);
    }

    public void mostrarPanel(String n) {
        if (n.equals("MENU_ADMIN")) {
            panelContenedor.add(new DashboardAdmin(this), "MENU_ADMIN");
            lblModuloActual.setText("|  ADMINISTRACIÓN");
        } else if (n.equals("MENU_USUARIO")) {
            panelContenedor.add(new DashboardUsuario(this), "MENU_USUARIO");
            lblModuloActual.setText("|  PORTAL PASAJERO");
        }

        cardLayout.show(panelContenedor, n);
        
        boolean publico = n.equals("INSTITUCIONAL") || n.equals("LOGIN") || n.equals("REGISTRO");
        barraNavegacion.setVisible(!publico);
        
        if (!publico) {
            lblUsuarioInfo.setText(SesionUsuario.getNombreExhibicion());
        }
        
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private void iniciarServicios() {
        new Timer(1000, e -> {
            lblReloj.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }).start();
    }
}