package vista.usuario;

import controlador.MetroManager;
import controlador.PasajeroController;
import modelo.Boleto;
import modelo.Estacion;
import modelo.Pasajero;
import utilidades.Estilos;
import utilidades.GeneradorQR;
import utilidades.RelojSistema;
import utilidades.SesionUsuario;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CompraBoletoPanel extends JPanel {

    private JComboBox<Estacion> cbOrigen;
    private JSpinner spCantidad;
    private JPanel panelPasajeros;
    private List<PasajeroInputPanel> inputsPasajeros;
    private JTextArea txtComprobante;
    private JLabel lblQR, lblInfoTren;
    private PasajeroController pasajeroController;
    private ConsultaRutaPanel consultaRutaPanel;

    public CompraBoletoPanel(ConsultaRutaPanel consultaRutaPanel) {
        this.consultaRutaPanel = consultaRutaPanel;
        this.pasajeroController = new PasajeroController();
        this.inputsPasajeros = new ArrayList<>();
        setLayout(new BorderLayout(25, 25));
        setOpaque(false);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // --- SECCIÓN IZQUIERDA: CONFIGURACIÓN ---
        JPanel panelConfig = Estilos.crearTarjeta();
        panelConfig.setLayout(new GridBagLayout());
        panelConfig.setPreferredSize(new Dimension(500, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTit = new JLabel("COMPRA DE BOLETOS");
        lblTit.setFont(Estilos.FUENTE_SUBTITULO);
        lblTit.setForeground(Estilos.AZUL_OSCURO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelConfig.add(lblTit, gbc);

        cbOrigen = new JComboBox<>();
        for (Estacion e : MetroManager.getInstancia().getRutaController().getLinea1()) {
            cbOrigen.addItem(e);
        }
        cbOrigen.addActionListener(e -> actualizarInfoTren());

        spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spCantidad.addChangeListener(e -> actualizarInputsPasajeros());

        gbc.gridwidth = 1;
        agregarFila(panelConfig, "Estación de Ingreso:", cbOrigen, 1, gbc);
        
        lblInfoTren = new JLabel("Calculando próximo tren...");
        lblInfoTren.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblInfoTren.setForeground(Estilos.ROJO_METRO);
        gbc.gridx = 1; gbc.gridy = 2;
        panelConfig.add(lblInfoTren, gbc);

        agregarFila(panelConfig, "N° Pasajeros:", spCantidad, 3, gbc);

        panelPasajeros = new JPanel();
        panelPasajeros.setLayout(new BoxLayout(panelPasajeros, BoxLayout.Y_AXIS));
        panelPasajeros.setOpaque(false);
        
        JScrollPane scrollPasajeros = new JScrollPane(panelPasajeros);
        scrollPasajeros.setBorder(BorderFactory.createTitledBorder("Detalle de Pasajeros"));
        scrollPasajeros.setOpaque(false);
        scrollPasajeros.getViewport().setOpaque(false);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panelConfig.add(scrollPasajeros, gbc);

        JButton btnComprar = Estilos.crearBotonAccion("CONFIRMAR COMPRA", Estilos.EXITO);
        btnComprar.setPreferredSize(new Dimension(0, 50));
        btnComprar.addActionListener(e -> realizarCompra());
        gbc.gridy = 5; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelConfig.add(btnComprar, gbc);

        // --- SECCIÓN DERECHA: COMPROBANTE ---
        JPanel panelDerecho = Estilos.crearTarjeta();
        panelDerecho.setLayout(new BorderLayout(15, 15));
        
        JLabel lblRes = new JLabel("COMPROBANTE");
        lblRes.setFont(Estilos.FUENTE_NEGRILLA);
        panelDerecho.add(lblRes, BorderLayout.NORTH);

        txtComprobante = new JTextArea();
        txtComprobante.setEditable(false);
        txtComprobante.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtComprobante.setBackground(new Color(250, 250, 250));
        txtComprobante.setBorder(BorderFactory.createLineBorder(Estilos.GRIS_BORDE));

        lblQR = new JLabel("El código QR se generará aquí", SwingConstants.CENTER);
        lblQR.setPreferredSize(new Dimension(200, 200));
        lblQR.setBorder(BorderFactory.createDashedBorder(Estilos.GRIS_BORDE));

        panelDerecho.add(new JScrollPane(txtComprobante), BorderLayout.CENTER);
        panelDerecho.add(lblQR, BorderLayout.SOUTH);

        add(panelConfig, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        actualizarInputsPasajeros();
        actualizarInfoTren();

        RelojSistema.getInstancia().agregarSuscriptor(e -> actualizarInfoTren());
    }

    private void actualizarInfoTren() {
        Estacion origen = (Estacion) cbOrigen.getSelectedItem();
        if (origen == null) return;

        int minNS = MetroManager.getInstancia().getSimulacionController().calcularTiempoLlegada(origen, true);
        int minSN = MetroManager.getInstancia().getSimulacionController().calcularTiempoLlegada(origen, false);
        
        String info = "Próximo tren: ";
        if (minNS == 0 || minSN == 0) info += "¡En andén!";
        else {
            int proximo = -1;
            if (minNS > 0 && minSN > 0) proximo = Math.min(minNS, minSN);
            else proximo = Math.max(minNS, minSN);
            
            if (proximo > 0) info += proximo + " min.";
            else info = "Sin trenes próximos";
        }
        lblInfoTren.setText(info);
    }

    private void agregarFila(JPanel p, String text, JComponent comp, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y;
        p.add(new JLabel(text), gbc);
        gbc.gridx = 1;
        p.add(comp, gbc);
    }

    private void actualizarInputsPasajeros() {
        panelPasajeros.removeAll();
        inputsPasajeros.clear();
        int cant = (int) spCantidad.getValue();
        
        for (int i = 1; i <= cant; i++) {
            boolean esTitular = (i == 1); // El primer pasajero siempre es el usuario logueado
            PasajeroInputPanel p = new PasajeroInputPanel(i, esTitular);
            inputsPasajeros.add(p);
            panelPasajeros.add(p);
            panelPasajeros.add(Box.createVerticalStrut(10));
        }
        panelPasajeros.revalidate();
        panelPasajeros.repaint();
    }

    private void realizarCompra() {
        Estacion origen = (Estacion) cbOrigen.getSelectedItem();

        StringBuilder sb = new StringBuilder();
        sb.append("      METRODATA UIO - TICKET      \n");
        sb.append("==================================\n");
        sb.append("ESTACION INGRESO: ").append(origen).append("\n");
        sb.append("VALIDEZ: Todo el sistema\n");
        sb.append("----------------------------------\n");

        double total = 0;
        Boleto bRef = null;

        for (PasajeroInputPanel input : inputsPasajeros) {
            String nombre, cedula, tipo;
            double precio;
            
            if (input.isTitular()) {
                Pasajero p = (Pasajero) SesionUsuario.usuarioLogueado;
                nombre = p.getNombre() + " " + p.getApellido() + " (Usted)";
                cedula = p.getCedula();
                tipo = p.getTipoTarifa();
                precio = p.getPrecioTarifa();
            } else if (input.esRegistrado()) {
                Pasajero p = pasajeroController.buscarPasajero(input.getCedula());
                if (p == null) {
                    JOptionPane.showMessageDialog(this, "Pasajero con CI " + input.getCedula() + " no encontrado.");
                    return;
                }
                nombre = p.getNombre() + " " + p.getApellido();
                cedula = p.getCedula();
                tipo = p.getTipoTarifa();
                precio = p.getPrecioTarifa();
            } else {
                nombre = "PASAJERO ACOMPAÑANTE";
                cedula = "N/A";
                tipo = input.getTarifaManual();
                precio = tipo.equals("Normal") ? 0.45 : 0.22;
            }

            Boleto b = new Boleto("B-" + (int)(Math.random()*9000), nombre, cedula, tipo, precio, origen.getNombre(), "SISTEMA ABIERTO");
            MetroManager.getInstancia().registrarVenta(b);
            bRef = b;
            total += precio;
            sb.append(String.format("%-18s [%s] $%.2f\n", nombre.length() > 18 ? nombre.substring(0, 15) + "..." : nombre, tipo, precio));
        }

        sb.append("----------------------------------\n");
        sb.append(String.format("TOTAL:             $%.2f\n", total));
        sb.append("==================================\n");

        txtComprobante.setText(sb.toString());
        if (bRef != null) {
            lblQR.setIcon(GeneradorQR.generarImagenQR(bRef.getInfoQR(), 180, bRef.getId()));
            lblQR.setText("");
        }

        // Automatización solicitada: Actualizar estación en el mapa
        consultaRutaPanel.setEstacionSeleccionada(origen);
    }

    private class PasajeroInputPanel extends JPanel {
        private JCheckBox chkRegistrado;
        private JTextField txtCedula;
        private JComboBox<String> cbTarifa;
        private boolean titular;

        public PasajeroInputPanel(int num, boolean titular) {
            this.titular = titular;
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            setOpaque(true);
            setBackground(titular ? new Color(232, 245, 233) : Color.WHITE);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Estilos.GRIS_BORDE));
            
            JLabel lblNum = new JLabel("#" + num);
            lblNum.setFont(Estilos.FUENTE_NEGRILLA);
            add(lblNum);

            if (titular) {
                JLabel lblTitular = new JLabel("Mi Boleto (Usuario en Sesión)");
                lblTitular.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblTitular.setForeground(new Color(46, 125, 50));
                add(lblTitular);
            } else {
                chkRegistrado = new JCheckBox("Registrado");
                chkRegistrado.setOpaque(false);
                txtCedula = new JTextField(8);
                txtCedula.setEnabled(false);
                cbTarifa = new JComboBox<>(new String[]{"Normal", "Reducida"});

                chkRegistrado.addActionListener(e -> {
                    txtCedula.setEnabled(chkRegistrado.isSelected());
                    cbTarifa.setEnabled(!chkRegistrado.isSelected());
                });

                add(chkRegistrado);
                add(new JLabel("CI:"));
                add(txtCedula);
                add(new JLabel("Tarifa:"));
                add(cbTarifa);
            }
        }

        public boolean isTitular() { return titular; }
        public boolean esRegistrado() { return chkRegistrado != null && chkRegistrado.isSelected(); }
        public String getCedula() { return txtCedula != null ? txtCedula.getText() : ""; }
        public String getTarifaManual() { return cbTarifa != null ? (String) cbTarifa.getSelectedItem() : "Normal"; }
    }
}