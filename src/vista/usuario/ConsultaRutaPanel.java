package vista.usuario;

import controlador.MetroManager;
import modelo.Estacion;
import modelo.Tren;
import utilidades.Estilos;
import utilidades.RelojSistema;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class ConsultaRutaPanel extends JPanel {

    private JPanel panelMapa;
    private JComboBox<Estacion> cbOrigen;
    private JComboBox<String> cbDireccion;
    private JLabel lblTiempoEstimado;

    public ConsultaRutaPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel header = Estilos.crearTarjeta();
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        cbOrigen = new JComboBox<>();
        for (Estacion e : MetroManager.getInstancia().getRutaController().getLinea1()) {
            cbOrigen.addItem(e);
        }
        
        cbDireccion = new JComboBox<>(new String[]{"Norte-Sur (Quitumbe)", "Sur-Norte (Labrador)"});
        
        lblTiempoEstimado = new JLabel("Seleccione su ubicación...");
        lblTiempoEstimado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTiempoEstimado.setForeground(Estilos.ROJO_METRO);

        header.add(new JLabel("MI ESTACIÓN:"));
        header.add(cbOrigen);
        // Destino y Dirección ocultos según requerimiento (solo origen importa)
        // header.add(new JLabel("DESTINO:"));
        // header.add(cbDireccion);
        header.add(Box.createHorizontalStrut(30));
        header.add(lblTiempoEstimado);

        panelMapa = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarMapa(g);
            }
        };
        panelMapa.setBackground(new Color(240, 240, 240));
        
        add(header, BorderLayout.NORTH);
        add(new JScrollPane(panelMapa), BorderLayout.CENTER);

        RelojSistema.getInstancia().agregarSuscriptor(e -> {
            actualizarTiempo();
            panelMapa.repaint();
        });
    }

    public void setEstacionSeleccionada(Estacion e) {
        cbOrigen.setSelectedItem(e);
        actualizarTiempo();
    }

    private void actualizarTiempo() {
        Estacion origen = (Estacion) cbOrigen.getSelectedItem();
        if (origen == null) return;
        
        int minNS = MetroManager.getInstancia().getSimulacionController().calcularTiempoLlegada(origen, true);
        int minSN = MetroManager.getInstancia().getSimulacionController().calcularTiempoLlegada(origen, false);
        
        int minutos = -1;
        if (minNS >= 0 && minSN >= 0) minutos = Math.min(minNS, minSN);
        else minutos = Math.max(minNS, minSN);

        if (minutos == -1) {
            lblTiempoEstimado.setText("Sin trenes próximos...");
        } else if (minutos == 0) {
            lblTiempoEstimado.setText("¡TREN EN ANDÉN!");
        } else {
            lblTiempoEstimado.setText("Llegada estimada: " + minutos + " min.");
        }
    }

    private void dibujarMapa(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Estacion> estaciones = MetroManager.getInstancia().getRutaController().getLinea1().toList();
        int xStart = 100;
        int yCentral = 300;
        int xGap = 150;
        int yOffsetVias = 40; // Distancia entre vía de ida y vía de regreso

        panelMapa.setPreferredSize(new Dimension(xStart + estaciones.size() * xGap + 100, 600));
        panelMapa.revalidate();

        // 1. DIBUJAR VÍAS (Doble vía)
        g2.setStroke(new BasicStroke(4));
        // Vía Norte-Sur (Arriba)
        g2.setColor(new Color(180, 180, 180));
        g2.drawLine(xStart, yCentral - yOffsetVias, xStart + (estaciones.size() - 1) * xGap, yCentral - yOffsetVias);
        // Vía Sur-Norte (Abajo)
        g2.drawLine(xStart, yCentral + yOffsetVias, xStart + (estaciones.size() - 1) * xGap, yCentral + yOffsetVias);

        // 2. DIBUJAR ESTACIONES (Andén Central)
        for (int i = 0; i < estaciones.size(); i++) {
            int x = xStart + i * xGap;
            
            // Conector entre vías y andén
            g2.setStroke(new BasicStroke(1));
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x, yCentral - yOffsetVias, x, yCentral + yOffsetVias);

            // Representación de la Estación
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(x - 12, yCentral - 20, 24, 40, 8, 8);
            g2.setColor(Estilos.AZUL_OSCURO);
            g2.drawRoundRect(x - 12, yCentral - 20, 24, 40, 8, 8);

            // Nombre Estación
            g2.setColor(Estilos.GRIS_TEXTO);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            AffineTransform old = g2.getTransform();
            g2.translate(x + 5, yCentral + 60);
            g2.rotate(Math.toRadians(45));
            g2.drawString(estaciones.get(i).getNombre(), 0, 0);
            g2.setTransform(old);
        }

        // 3. DIBUJAR TRENES (Cada uno en su vía)
        List<Tren> trenes = MetroManager.getInstancia().getSimulacionController().getTrenesActivos();
        for (Tren t : trenes) {
            int idxActual = obtenerIndice(estaciones, t.getEstacionActual().dato);
            int xTren;
            int yTren;
            
            if (t.isDireccionNorteSur()) {
                xTren = xStart + (int) ((idxActual + t.getProgreso()) * xGap);
                yTren = yCentral - yOffsetVias;
                g2.setColor(Estilos.ROJO_METRO);
            } else {
                xTren = xStart + (int) ((idxActual - t.getProgreso()) * xGap);
                yTren = yCentral + yOffsetVias;
                g2.setColor(Estilos.AZUL_OSCURO);
            }

            // Cuerpo del Tren
            g2.fillRoundRect(xTren - 25, yTren - 12, 50, 24, 10, 10);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            // Eliminado < > del nombre del tren
            g2.drawString(t.getId(), xTren - 15, yTren + 5);
        }
        
        // 4. LEYENDA (Sin Norte/Sur)
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        g2.setColor(Estilos.ROJO_METRO);
        g2.drawString("VIA 1 (Destino Quitumbe)", xStart, 50);
        g2.setColor(Estilos.AZUL_OSCURO);
        g2.drawString("VIA 2 (Destino Labrador)", xStart, 75);
    }

    private int obtenerIndice(List<Estacion> lista, Estacion e) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getNombre().equals(e.getNombre())) return i;
        }
        return 0;
    }
}