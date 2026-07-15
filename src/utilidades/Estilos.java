package utilidades;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Estilos {
    public static final Color ROJO_METRO = new Color(200, 16, 46);
    public static final Color AZUL_OSCURO = new Color(26, 35, 126);
    public static final Color BLANCO = Color.WHITE;
    public static final Color GRIS_FONDO = new Color(245, 247, 250);
    public static final Color GRIS_CLARO = new Color(242, 242, 242);
    public static final Color GRIS_BORDE = new Color(225, 225, 225);
    public static final Color GRIS_TEXTO = new Color(70, 70, 70);
    public static final Color EXITO = new Color(40, 167, 69);
    public static final Color ERROR = new Color(220, 53, 69);
    public static final Color NEGRO = Color.BLACK;

    public static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FUENTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FUENTE_NEGRILLA = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 14);

    public static void aplicarEstiloGlobal() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    public static JButton crearBotonPrincipal(String texto) {
        JButton btn = new JButton(texto.toUpperCase());
        btn.setFont(FUENTE_BOTON);
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_OSCURO);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setForeground(AZUL_OSCURO);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(new LineBorder(AZUL_OSCURO, 1));
        return btn;
    }

    public static JButton crearBotonAccion(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(fondo);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 38));
        return btn;
    }

    public static JPanel crearTarjeta() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        return p;
    }

    public static void darEstiloTabla(JTable tabla) {
        tabla.setFont(FUENTE_NORMAL);
        tabla.setRowHeight(45);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(230, 230, 230));
        tabla.setSelectionBackground(new Color(232, 240, 254));
        tabla.setSelectionForeground(Color.BLACK);
        
        JTableHeader h = tabla.getTableHeader();
        h.setFont(FUENTE_NEGRILLA);
        h.setBackground(GRIS_FONDO);
        h.setOpaque(false);
        h.setPreferredSize(new Dimension(0, 45));
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, GRIS_BORDE));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.setDefaultRenderer(Object.class, centerRenderer);
        
        tabla.setOpaque(false);
        if (tabla.getDefaultRenderer(Object.class) instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer)tabla.getDefaultRenderer(Object.class)).setOpaque(false);
        }
    }
}