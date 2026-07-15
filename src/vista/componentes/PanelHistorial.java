package vista.componentes;

import estructuras.Pila;
import utilidades.Estilos;

import javax.swing.*;
import java.awt.*;

public class PanelHistorial extends JPanel {
    private Pila<String> historial;

    public PanelHistorial(Pila<String> historial) {
        this.historial = historial;
        setBackground(Estilos.BLANCO);
        setPreferredSize(new Dimension(150, 0));
        setBorder(BorderFactory.createTitledBorder("Navegación"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        int y = 50;
        // Dibujaremos los últimos 5 elementos de la pila para no saturar
        Pila<String> copia = clonarPila(historial);
        
        while (!copia.estaVacia()) {
            String pagina = copia.pop();
            g2.setColor(Estilos.ROJO_METRO);
            g2.fillRoundRect(10, y, 130, 30, 10, 10);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.drawString(pagina, 20, y + 20);
            
            y += 40;
            if (y > getHeight() - 50) break;
        }
    }

    private Pila<String> clonarPila(Pila<String> original) {
        Pila<String> aux = new Pila<>();
        Pila<String> copia = new Pila<>();
        // Invertimos para mantener el orden visual
        while (!original.estaVacia()) aux.push(original.pop());
        while (!aux.estaVacia()) {
            String s = aux.pop();
            original.push(s);
            copia.push(s);
        }
        return copia;
    }

    public void actualizar() { repaint(); }
}
