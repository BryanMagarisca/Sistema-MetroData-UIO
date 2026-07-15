package vista.administrador;

import controlador.MetroManager;
import modelo.Boleto;
import utilidades.Estilos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistorialVentasPanel extends JPanel {

    private DefaultTableModel model;

    public HistorialVentasPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        
        JPanel card = Estilos.crearTarjeta();
        card.setLayout(new BorderLayout(10, 10));
        
        JLabel lbl = new JLabel("HISTORIAL DE VENTAS Y PASAJEROS");
        lbl.setFont(Estilos.FUENTE_SUBTITULO);
        card.add(lbl, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID BOLETO", "PASAJERO", "CÉDULA", "ENTRADA", "TARIFA", "PRECIO"}, 0);
        JTable tabla = new JTable(model);
        Estilos.darEstiloTabla(tabla);
        card.add(new JScrollPane(tabla), BorderLayout.CENTER);
        
        JButton btnRefrescar = Estilos.crearBotonSecundario("Actualizar Datos");
        btnRefrescar.addActionListener(e -> cargarDatos());
        card.add(btnRefrescar, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
        cargarDatos();
    }

    private void cargarDatos() {
        model.setRowCount(0);
        List<Boleto> ventas = MetroManager.getInstancia().getVentasRealizadas();
        for (Boleto b : ventas) {
            model.addRow(new Object[]{b.getId(), b.getNombrePasajero(), b.getCedula(), b.getOrigen(), b.getTipoTarifa(), String.format("$%.2f", b.getPrecio())});
        }
    }
}