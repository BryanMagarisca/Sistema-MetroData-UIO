package vista.administrador;

import controlador.MetroManager;
import modelo.Estacion;
import modelo.Tren;
import utilidades.Estilos;
import utilidades.RelojSistema;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GestionInfraestructuraPanel extends JPanel {

    private DefaultTableModel modelEstaciones, modelTrenes;
    private JTable tablaEstaciones, tablaTrenes;

    public GestionInfraestructuraPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        inicializarComponentes();
        
        // Sincronizar actualización con el reloj del sistema (igual que el recorrido)
        RelojSistema.getInstancia().agregarSuscriptor(e -> actualizarTablas());
    }

    private void inicializarComponentes() {
        // PANEL SUPERIOR: ESTACIONES
        JPanel pEst = new JPanel(new BorderLayout(10, 10));
        pEst.setOpaque(false);
        pEst.add(new JLabel("LISTADO DE ESTACIONES OPERATIVAS"), BorderLayout.NORTH);
        
        modelEstaciones = new DefaultTableModel(new String[]{"NOMBRE ESTACIÓN", "SERVICIOS"}, 0);
        tablaEstaciones = new JTable(modelEstaciones);
        Estilos.darEstiloTabla(tablaEstaciones);
        pEst.add(new JScrollPane(tablaEstaciones), BorderLayout.CENTER);
        
        JButton btnAddE = Estilos.crearBotonAccion("Nueva Estación", Estilos.AZUL_OSCURO);
        btnAddE.addActionListener(e -> agregarEstacion());
        pEst.add(btnAddE, BorderLayout.SOUTH);

        // PANEL INFERIOR: TRENES (SINCRONIZADO)
        JPanel pTren = new JPanel(new BorderLayout(10, 10));
        pTren.setOpaque(false);
        pTren.add(new JLabel("MONITOREO DE FLOTA EN TIEMPO REAL"), BorderLayout.NORTH);
        
        modelTrenes = new DefaultTableModel(new String[]{"ID TREN", "ESTADO ACTUAL", "UBICACIÓN / TRAYECTO"}, 0);
        tablaTrenes = new JTable(modelTrenes);
        Estilos.darEstiloTabla(tablaTrenes);
        pTren.add(new JScrollPane(tablaTrenes), BorderLayout.CENTER);
        
        JButton btnAddT = Estilos.crearBotonAccion("Crear Nuevo Tren", Estilos.EXITO);
        btnAddT.addActionListener(e -> agregarTren());
        pTren.add(btnAddT, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pEst, pTren);
        split.setDividerLocation(300);
        split.setOpaque(false);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        actualizarTablas();
    }

    private void agregarEstacion() {
        String n = JOptionPane.showInputDialog(this, "Nombre de la estación:");
        if (n != null && !n.isEmpty()) {
            boolean c = JOptionPane.showConfirmDialog(this, "¿Incluir cochera física?") == JOptionPane.YES_OPTION;
            MetroManager.getInstancia().getRutaController().getLinea1().agregarAlFinal(new Estacion(n, c));
            actualizarTablas();
        }
    }

    private void agregarTren() {
        String id = "T-N" + (System.currentTimeMillis() % 1000);
        Estacion lab = MetroManager.getInstancia().getRutaController().getEstacionLabrador();
        lab.getCochera().agregarTren(new Tren(id, MetroManager.getInstancia().getRutaController().getLinea1().getCabeza(), true));
        actualizarTablas();
        JOptionPane.showMessageDialog(this, "Tren " + id + " inyectado en Cochera Norte.");
    }

    private void actualizarTablas() {
        // Actualizar Estaciones
        modelEstaciones.setRowCount(0);
        for (Estacion e : MetroManager.getInstancia().getRutaController().getLinea1()) {
            modelEstaciones.addRow(new Object[]{e.getNombre(), e.tieneCochera() ? "Cochera LIFO" : "Andén de Paso"});
        }

        // Actualizar Trenes Sincronizados con el Recorrido
        modelTrenes.setRowCount(0);
        
        // 1. Obtener trenes en las cocheras (Pilas)
        listarTrenesCochera(MetroManager.getInstancia().getRutaController().getEstacionLabrador());
        listarTrenesCochera(MetroManager.getInstancia().getRutaController().getEstacionQuitumbe());
        
        // 2. Obtener trenes ACTIVOS (los que se ven en el mapa animado)
        for (Tren t : MetroManager.getInstancia().getSimulacionController().getTrenesActivos()) {
            String trayecto = t.isDireccionNorteSur() ? "Hacia Quitumbe" : "Hacia Labrador";
            String ubicacion = t.getEstacionActual().dato.getNombre();
            modelTrenes.addRow(new Object[]{t.getId(), "EN VÍA (Circulando)", ubicacion + " [" + trayecto + "]"});
        }
    }

    private void listarTrenesCochera(Estacion e) {
        if (!e.tieneCochera()) return;
        
        // Usar copia temporal para no alterar la estructura LIFO original al visualizar
        estructuras.Pila<Tren> temp = new estructuras.Pila<>();
        List<Tren> listaCochera = new ArrayList<>();
        
        while(!e.getCochera().getPilaTrenes().estaVacia()) {
            Tren t = e.getCochera().getPilaTrenes().pop();
            listaCochera.add(t);
            temp.push(t);
        }
        // Restaurar pila
        while(!temp.estaVacia()) e.getCochera().getPilaTrenes().push(temp.pop());
        
        // Mostrar en tabla
        for (Tren t : listaCochera) {
            modelTrenes.addRow(new Object[]{t.getId(), "ESTACIONADO", "Cochera " + e.getNombre()});
        }
    }
}