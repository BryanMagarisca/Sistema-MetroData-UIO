package vista.administrador;

import utilidades.Estilos;
import utilidades.SesionUsuario;
import vista.MainFrame;
import vista.pasajeros.GestionPasajerosPanel;
import vista.reportes.PanelReportes;
import vista.usuario.ConsultaRutaPanel;
import javax.swing.*;
import java.awt.*;

public class DashboardAdmin extends JPanel {

    private MainFrame mainFrame;
    private JPanel panelEscritorio;
    private CardLayout cardLayoutInterno;
    private JButton btnActual;

    public DashboardAdmin(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Estilos.GRIS_FONDO);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Estilos.AZUL_OSCURO);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTit = new JLabel("ADMIN CONTROL");
        lblTit.setForeground(Color.WHITE);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblTit);
        sidebar.add(Box.createVerticalStrut(30));

        agregarBotonMenu(sidebar, "Reportes", "R");
        agregarBotonMenu(sidebar, "Pasajeros", "P");
        agregarBotonMenu(sidebar, "Pilas Cochera", "M");
        agregarBotonMenu(sidebar, "Infraestructura", "I");
        agregarBotonMenu(sidebar, "Historial Ventas", "H");
        agregarBotonMenu(sidebar, "Ver Recorrido", "MAP");

        add(sidebar, BorderLayout.WEST);

        cardLayoutInterno = new CardLayout();
        panelEscritorio = new JPanel(cardLayoutInterno);
        panelEscritorio.setOpaque(false);
        panelEscritorio.setBorder(null);

        panelEscritorio.add(new PanelReportes(), "R");
        panelEscritorio.add(new GestionPasajerosPanel(), "P");
        panelEscritorio.add(new PanelMonitoreo(), "M");
        panelEscritorio.add(new GestionInfraestructuraPanel(), "I");
        panelEscritorio.add(new HistorialVentasPanel(), "H");
        panelEscritorio.add(new ConsultaRutaPanel(), "MAP");

        add(panelEscritorio, BorderLayout.CENTER);
        
        cardLayoutInterno.show(panelEscritorio, "R");
    }

    private void agregarBotonMenu(JPanel menu, String texto, String card) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(Estilos.AZUL_OSCURO);
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
                btnActual.setBackground(Estilos.AZUL_OSCURO);
                btnActual.setForeground(new Color(200, 200, 200));
            }
            btn.setBackground(new Color(63, 81, 181));
            btn.setForeground(Color.WHITE);
            btnActual = btn;
            cardLayoutInterno.show(panelEscritorio, card);
        });

        menu.add(btn);
        menu.add(Box.createVerticalStrut(5));
    }
}