package vista.administrador;

import utilidades.Estilos;
import javax.swing.*;
import java.awt.*;

public class GestionAdminsPanel extends JPanel {
    public GestionAdminsPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        
        JPanel card = Estilos.crearTarjeta();
        card.setLayout(new GridBagLayout());
        
        JLabel lbl = new JLabel("GESTIÓN DE ADMINISTRADORES");
        lbl.setFont(Estilos.FUENTE_SUBTITULO);
        card.add(lbl);
        
        add(card, BorderLayout.CENTER);
    }
}