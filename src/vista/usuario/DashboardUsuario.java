package vista.usuario;

import utilidades.Estilos;
import vista.MainFrame;
import javax.swing.*;
import java.awt.*;

public class DashboardUsuario extends JPanel {

    private MainFrame mainFrame;
    private JPanel panelEscritorio;
    private CardLayout cardLayoutInterno;
    private JButton btnActual;
    private CompraBoletoPanel compraBoletoPanel;
    private ConsultaRutaPanel consultaRutaPanel;

    public DashboardUsuario(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Estilos.GRIS_FONDO);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(33, 33, 33));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTit = new JLabel("MI PORTAL");
        lblTit.setForeground(Color.LIGHT_GRAY);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblTit);
        sidebar.add(Box.createVerticalStrut(30));

        // Invertimos el orden en el menú
        agregarBotonMenu(sidebar, "Comprar Boletos", "CB");
        agregarBotonMenu(sidebar, "Ver Mapa y Rutas", "CR");

        add(sidebar, BorderLayout.WEST);

        cardLayoutInterno = new CardLayout();
        panelEscritorio = new JPanel(cardLayoutInterno);
        panelEscritorio.setOpaque(false);
        panelEscritorio.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        consultaRutaPanel = new ConsultaRutaPanel();
        compraBoletoPanel = new CompraBoletoPanel(consultaRutaPanel);

        panelEscritorio.add(compraBoletoPanel, "CB");
        panelEscritorio.add(consultaRutaPanel, "CR");

        add(panelEscritorio, BorderLayout.CENTER);
        
        // Mostrar Compra Boletos primero
        cardLayoutInterno.show(panelEscritorio, "CB");
    }

    private void agregarBotonMenu(JPanel menu, String texto, String card) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(180, 180, 180));
        btn.setBackground(new Color(33, 33, 33));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        btn.addActionListener(e -> {
            if (btnActual != null) {
                btnActual.setBackground(new Color(33, 33, 33));
                btnActual.setForeground(new Color(180, 180, 180));
            }
            btn.setBackground(Estilos.ROJO_METRO);
            btn.setForeground(Color.WHITE);
            btnActual = btn;
            cardLayoutInterno.show(panelEscritorio, card);
        });

        menu.add(btn);
        menu.add(Box.createVerticalStrut(5));
    }
}