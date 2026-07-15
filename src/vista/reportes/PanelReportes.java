package vista.reportes;

import controlador.MetroManager;
import controlador.PasajeroController;
import modelo.Boleto;
import utilidades.Estilos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelReportes extends JPanel {

    private PasajeroController pasajeroController;
    private DefaultTableModel modelResumen;

    public PanelReportes() {
        this.pasajeroController = new PasajeroController();
        setLayout(new BorderLayout(0, 30));
        setOpaque(false);
        setBorder(null);
        inicializarComponentes();
        iniciarAutoActualizacion();
    }

    private void inicializarComponentes() {
        JLabel lblTitulo = new JLabel("REPORTE DE OPERACIONES");
        lblTitulo.setFont(Estilos.FUENTE_SUBTITULO);
        lblTitulo.setForeground(Estilos.AZUL_OSCURO);
        add(lblTitulo, BorderLayout.NORTH);

        String[] columnas = {"INDICADOR", "VALOR ACTUAL", "DETALLE ESTADO"};
        modelResumen = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(modelResumen);
        Estilos.darEstiloTabla(tabla);
        
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    private void iniciarAutoActualizacion() {
        new Timer(2000, e -> actualizarDatos()).start();
    }

    private void actualizarDatos() {
        modelResumen.setRowCount(0);
        int pasajeros = pasajeroController.listarPasajerosInorden().size();
        modelResumen.addRow(new Object[]{"Pasajeros Totales", pasajeros, "Base de Datos Activa"});

        List<Boleto> ventas = MetroManager.getInstancia().getVentasRealizadas();
        modelResumen.addRow(new Object[]{"Boletos Vendidos", ventas.size(), "Historial Consolidado"});

        double ingresos = ventas.stream().mapToDouble(Boleto::getPrecio).sum();
        modelResumen.addRow(new Object[]{"Recaudacion Total", String.format("$ %.2f", ingresos), "Contabilizado"});

        int trenes = MetroManager.getInstancia().getSimulacionController().getTrenesActivos().size();
        modelResumen.addRow(new Object[]{"Trenes en Via", trenes, "Monitoreo Real-Time"});

        int viajes = MetroManager.getInstancia().getSimulacionController().getViajesCompletados();
        modelResumen.addRow(new Object[]{"Ciclos Finalizados", viajes, "Operacion LIFO"});
    }
}