package vista.administrador;

import controlador.MetroManager;
import modelo.Cochera;
import modelo.Estacion;
import modelo.Tren;
import utilidades.Estilos;
import utilidades.RelojSistema;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelMonitoreo extends JPanel {

    private JPanel panelCocheras;

    public PanelMonitoreo() {
        setLayout(new BorderLayout(0, 25));
        setOpaque(false);
        
        // Cabecera Informativa
        JPanel header = Estilos.crearTarjeta();
        header.setLayout(new BorderLayout());
        JLabel lblTitulo = new JLabel("MONITOREO DE COCHERAS - ESTRUCTURA PILA (LIFO)");
        lblTitulo.setFont(Estilos.FUENTE_SUBTITULO);
        lblTitulo.setForeground(Estilos.AZUL_OSCURO);
        
        JLabel lblDesc = new JLabel("Representación de los trenes estacionados en los extremos de la Línea 1");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        header.add(lblTitulo, BorderLayout.NORTH);
        header.add(lblDesc, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Panel central que emula la disposición del mapa
        panelCocheras = new JPanel(new GridLayout(1, 2, 40, 0));
        panelCocheras.setOpaque(false);
        add(panelCocheras, BorderLayout.CENTER);

        RelojSistema.getInstancia().agregarSuscriptor(e -> actualizar());
        actualizar();
    }

    private void actualizar() {
        panelCocheras.removeAll();
        Estacion lab = MetroManager.getInstancia().getRutaController().getEstacionLabrador();
        Estacion qui = MetroManager.getInstancia().getRutaController().getEstacionQuitumbe();
        
        // El Labrador (Norte) a la izquierda, Quitumbe (Sur) a la derecha, igual que el mapa
        panelCocheras.add(crearVistaCochera(lab.getCochera(), true));
        panelCocheras.add(crearVistaCochera(qui.getCochera(), false));
        
        panelCocheras.revalidate();
        panelCocheras.repaint();
    }

    private JPanel crearVistaCochera(Cochera cochera, boolean esNorte) {
        JPanel contenedor = Estilos.crearTarjeta();
        contenedor.setLayout(new BorderLayout(10, 15));

        // Info Superior con Iconografía acorde al sentido
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        String icono = esNorte ? "[NORTE]" : "[SUR]";
        JLabel lblNombre = new JLabel(icono + " " + cochera.getNombre().toUpperCase());
        lblNombre.setFont(Estilos.FUENTE_NEGRILLA);
        lblNombre.setForeground(esNorte ? Estilos.ROJO_METRO : Estilos.AZUL_OSCURO);
        
        JLabel lblOperacion = new JLabel("Última Op: " + cochera.getUltimaOperacion());
        lblOperacion.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        info.add(lblNombre);
        info.add(lblOperacion);

        // Representación Visual de la Pila Estilizada como en el Recorrido
        JPanel areaDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarPilaEstilizada(g, cochera, getWidth(), getHeight(), esNorte);
            }
        };
        areaDibujo.setBackground(new Color(245, 245, 245));
        areaDibujo.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        contenedor.add(info, BorderLayout.NORTH);
        contenedor.add(areaDibujo, BorderLayout.CENTER);
        
        return contenedor;
    }

    private void dibujarPilaEstilizada(Graphics g, Cochera cochera, int w, int h, boolean esNorte) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Tren> trenes = extraerLista(cochera);
        int rectW = 200;
        int rectH = 40;
        int x = (w - rectW) / 2;
        int yBase = h - 60;

        // Dibujar "Vía de Estacionamiento"
        g2.setColor(new Color(150, 150, 150));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x - 20, yBase + rectH + 10, x + rectW + 20, yBase + rectH + 10);

        for (int i = 0; i < trenes.size(); i++) {
            int yPos = yBase - (i * (rectH + 8));
            
            // Si es el de arriba (Cima), usar el estilo del mapa
            if (i == trenes.size() - 1) {
                // Sombra
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillRoundRect(x + 5, yPos + 5, rectW, rectH, 12, 12);
                
                // Cuerpo Tren (Color según dirección de salida)
                g2.setColor(esNorte ? Estilos.ROJO_METRO : Estilos.AZUL_OSCURO);
                g2.fillRoundRect(x, yPos, rectW, rectH, 12, 12);
                
                // Indicador de Cima
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String flecha = esNorte ? "SALIDA NORTE" : "SALIDA SUR";
                g2.drawString(flecha + " - " + trenes.get(i).getId(), x + 25, yPos + 25);
                
                // Etiqueta Cima
                g2.setColor(new Color(255, 255, 255, 180));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                g2.drawString("TOP (LIFO)", x + rectW - 65, yPos + 12);
            } else {
                // Trenes debajo en la pila (Grisáceos / Inactivos)
                g2.setColor(new Color(200, 200, 200));
                g2.fillRoundRect(x, yPos, rectW, rectH, 8, 8);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(x, yPos, rectW, rectH, 8, 8);
                
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.drawString(trenes.get(i).getId(), x + 20, yPos + 25);
            }
        }
        
        // Si está vacía
        if (trenes.isEmpty()) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            g2.drawString("Cochera Vacía", w/2 - 50, h/2);
        }
    }

    private List<Tren> extraerLista(Cochera c) {
        List<Tren> lista = new ArrayList<>();
        estructuras.Pila<Tren> temp = new estructuras.Pila<>();
        // Vaciamos a temporal para obtener el orden
        while (!c.getPilaTrenes().estaVacia()) {
            Tren t = c.getPilaTrenes().pop();
            lista.add(t);
            temp.push(t);
        }
        // Restauramos
        while (!temp.estaVacia()) {
            c.getPilaTrenes().push(temp.pop());
        }
        // Invertimos para que el primer elemento sea el fondo de la pila y el último sea la cima
        java.util.Collections.reverse(lista);
        return lista;
    }
}