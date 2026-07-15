package vista.componentes;

import estructuras.ListaDobleEnlazada;
import modelo.Estacion;
import modelo.Tren;
import utilidades.Estilos;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelMapaMetro extends JPanel {
    private ListaDobleEnlazada<Estacion> linea;
    private List<Tren> trenes;

    public PanelMapaMetro(ListaDobleEnlazada<Estacion> linea) {
        this.linea = linea;
        setBackground(Estilos.BLANCO);
        setPreferredSize(new Dimension(1300, 350));
    }

    public void actualizarUI(List<Tren> trenes) {
        this.trenes = trenes;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int yBase = getHeight() / 2;
        int xStart = 60;
        int gap = (getWidth() - 150) / (linea.getTamaño() - 1);

        // 1. Dibujar Doble Vía (Túneles)
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Estilos.GRIS_CLARO);
        g2.drawLine(xStart, yBase - 30, xStart + (linea.getTamaño()-1)*gap, yBase - 30); 
        g2.drawLine(xStart, yBase + 30, xStart + (linea.getTamaño()-1)*gap, yBase + 30); 

        // 2. Dibujar Estaciones
        int i = 0;
        for (Estacion est : linea) {
            int x = xStart + (i * gap);
            g2.setColor(est.tieneCochera() ? Estilos.ROJO_METRO : Color.WHITE);
            g2.fillOval(x - 10, yBase - 10, 20, 20);
            g2.setColor(Estilos.NEGRO);
            g2.drawOval(x - 10, yBase - 10, 20, 20);
            
            // Nombre de la estación
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2.drawString(est.getNombre(), x - 20, yBase - 50);
            i++;
        }

        // 3. Dibujar Trenes con Movimiento Continuo
        if (trenes != null) {
            for (Tren t : trenes) {
                dibujarTren(g2, t, xStart, yBase, gap);
            }
        }
    }

    private void dibujarTren(Graphics2D g2, Tren t, int xStart, int yBase, int gap) {
        int idxActual = obtenerIndiceEstacion(t.getEstacionActual().dato);
        
        // Calculamos posición X interpolada
        int xActual = xStart + (idxActual * gap);
        int xSig = xStart + ((t.isDireccionNorteSur() ? idxActual + 1 : idxActual - 1) * gap);
        
        // INTERPOLACIÓN: xFinal = xInicio + (distancia * progreso)
        int xTren = (int) (xActual + (xSig - xActual) * t.getProgreso());
        int yTren = t.isDireccionNorteSur() ? yBase - 30 : yBase + 30;

        // Cuerpo Tren
        g2.setColor(Estilos.ROJO_METRO);
        g2.fillRoundRect(xTren - 15, yTren - 8, 30, 16, 5, 5);
        
        // ID
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 9));
        g2.drawString(t.getId(), xTren - 12, yTren - 12);
    }

    private int obtenerIndiceEstacion(Estacion e) {
        int i = 0;
        for (Estacion est : linea) {
            if (est.getNombre().equals(e.getNombre())) return i;
            i++;
        }
        return 0;
    }
}
