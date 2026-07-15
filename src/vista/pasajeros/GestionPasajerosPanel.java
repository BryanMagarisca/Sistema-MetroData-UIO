package vista.pasajeros;

import controlador.PasajeroController;
import modelo.Pasajero;
import utilidades.Estilos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GestionPasajerosPanel extends JPanel {

    private PasajeroController controller;
    private JTextField txtCedula, txtNombre, txtApellido, txtDomicilio, txtFechaNac, txtCorreo;
    private JPasswordField txtPassword;
    private JTable tablaPasajeros;
    private DefaultTableModel tableModel;
    private JButton btnGuardar;
    private boolean modoEdicion = false;

    public GestionPasajerosPanel() {
        this.controller = new PasajeroController();
        setLayout(new BorderLayout(25, 25));
        setOpaque(false);
        setBorder(null);
        inicializarComponentes();
        cargarDatosTabla();
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setPreferredSize(new Dimension(320, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTit = new JLabel("DATOS PASAJERO");
        lblTit.setFont(Estilos.FUENTE_NEGRILLA);
        gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(0, 0, 20, 0);
        panelForm.add(lblTit, gbc);

        gbc.insets = new Insets(6, 5, 6, 5);
        txtCedula = crearCampo(panelForm, "Cedula:", 1, gbc);
        txtNombre = crearCampo(panelForm, "Nombre:", 2, gbc);
        txtApellido = crearCampo(panelForm, "Apellido:", 3, gbc);
        txtDomicilio = crearCampo(panelForm, "Domicilio:", 4, gbc);
        txtFechaNac = crearCampo(panelForm, "F. Nac:", 5, gbc);
        txtCorreo = crearCampo(panelForm, "Correo:", 6, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        panelForm.add(new JLabel("Clave:"), gbc);
        txtPassword = new JPasswordField();
        gbc.gridx = 1;
        panelForm.add(txtPassword, gbc);

        btnGuardar = Estilos.crearBotonAccion("GUARDAR", Estilos.EXITO);
        btnGuardar.addActionListener(e -> registrar());
        gbc.gridy = 8; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(20, 5, 5, 5);
        panelForm.add(btnGuardar, gbc);

        JPanel panelDerecho = new JPanel(new BorderLayout(0, 15));
        panelDerecho.setOpaque(false);

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);
        
        JPanel pBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBusqueda.setOpaque(false);
        JTextField txtBuscar = new JTextField(12);
        JButton btnBuscar = Estilos.crearBotonAccion("BUSCAR", Estilos.AZUL_OSCURO);
        JButton btnLimpiar = Estilos.crearBotonAccion("LIMPIAR", Color.GRAY);
        pBusqueda.add(new JLabel("CI: "));
        pBusqueda.add(txtBuscar);
        pBusqueda.add(Box.createHorizontalStrut(5));
        pBusqueda.add(btnBuscar);
        pBusqueda.add(Box.createHorizontalStrut(5));
        pBusqueda.add(btnLimpiar);
        
        JPanel pAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pAcciones.setOpaque(false);
        JButton btnMod = Estilos.crearBotonAccion("MODIFICAR", new Color(255, 140, 0));
        JButton btnDel = Estilos.crearBotonAccion("ELIMINAR", Estilos.ERROR);
        pAcciones.add(btnMod);
        pAcciones.add(btnDel);

        barra.add(pBusqueda, BorderLayout.WEST);
        barra.add(pAcciones, BorderLayout.EAST);

        btnBuscar.addActionListener(e -> {
            if (txtBuscar.getText().trim().isEmpty()) {
                limpiarYHabiltar();
            } else {
                buscar(txtBuscar.getText().trim());
            }
        });
        
        btnLimpiar.addActionListener(e -> limpiarYHabiltar());
        
        btnMod.addActionListener(e -> habilitarParaModificar());
        btnDel.addActionListener(e -> eliminarSeleccionado());

        tableModel = new DefaultTableModel(new String[]{"CEDULA", "NOMBRE", "APELLIDO", "TARIFA"}, 0);
        tablaPasajeros = new JTable(tableModel);
        Estilos.darEstiloTabla(tablaPasajeros);
        
        JScrollPane scroll = new JScrollPane(tablaPasajeros);
        scroll.setBorder(null);
        panelDerecho.add(barra, BorderLayout.NORTH);
        panelDerecho.add(scroll, BorderLayout.CENTER);

        add(panelForm, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
        
        setCamposEditables(true);
    }

    private JTextField crearCampo(JPanel p, String etiqueta, int y, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        p.add(new JLabel(etiqueta), gbc);
        JTextField tf = new JTextField();
        gbc.gridx = 1;
        p.add(tf, gbc);
        return tf;
    }

    private void setCamposEditables(boolean valor) {
        txtCedula.setEditable(valor && !modoEdicion);
        txtNombre.setEditable(valor);
        txtApellido.setEditable(valor);
        txtDomicilio.setEditable(valor);
        txtFechaNac.setEditable(valor);
        txtCorreo.setEditable(valor);
        txtPassword.setEditable(valor);
        btnGuardar.setEnabled(valor);
    }

    private void cargarDatosTabla() {
        tableModel.setRowCount(0);
        for (Pasajero p : controller.listarPasajerosInorden()) {
            tableModel.addRow(new Object[]{p.getCedula(), p.getNombre(), p.getApellido(), p.getTipoTarifa()});
        }
    }

    private void registrar() {
        try {
            LocalDate f = LocalDate.parse(txtFechaNac.getText());
            if (controller.registrarPasajeroFull(txtCedula.getText(), txtNombre.getText(), txtApellido.getText(), "", txtDomicilio.getText(), f, txtCorreo.getText(), new String(txtPassword.getPassword()))) {
                cargarDatosTabla(); 
                String msj = modoEdicion ? "Se modificó con éxito" : "Pasajero guardado con éxito";
                JOptionPane.showMessageDialog(this, msj);
                limpiarYHabiltar();
            }
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error en los datos. Use AAAA-MM-DD"); }
    }

    private void habilitarParaModificar() {
        if (txtCedula.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Busque un pasajero primero.");
            return;
        }
        modoEdicion = true;
        setCamposEditables(true);
        JOptionPane.showMessageDialog(this, "Campos habilitados. Realice los cambios y presione GUARDAR.");
    }

    private void buscar(String c) {
        Pasajero p = controller.buscarPasajero(c);
        if (p != null) {
            txtCedula.setText(p.getCedula());
            txtNombre.setText(p.getNombre());
            txtApellido.setText(p.getApellido());
            txtDomicilio.setText(p.getDomicilio());
            txtFechaNac.setText(p.getFechaNacimiento().toString());
            txtCorreo.setText(p.getCorreo());
            txtPassword.setText(p.getPassword());

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(c)) {
                    tablaPasajeros.setRowSelectionInterval(i, i);
                    break;
                }
            }
            modoEdicion = false;
            setCamposEditables(false);
        } else {
            JOptionPane.showMessageDialog(this, "Pasajero no encontrado.");
        }
    }

    private void eliminarSeleccionado() {
        int f = tablaPasajeros.getSelectedRow();
        if (f != -1) {
            controller.eliminarPasajero((String) tableModel.getValueAt(f, 0));
            cargarDatosTabla(); 
            limpiarYHabiltar();
            JOptionPane.showMessageDialog(this, "Pasajero eliminado.");
        }
    }

    private void limpiarYHabiltar() {
        modoEdicion = false;
        txtCedula.setText(""); txtNombre.setText(""); txtApellido.setText("");
        txtDomicilio.setText(""); txtFechaNac.setText(""); txtCorreo.setText(""); txtPassword.setText("");
        setCamposEditables(true);
    }
}