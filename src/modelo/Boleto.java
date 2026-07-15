package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Boleto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String nombrePasajero;
    private String cedula;
    private String tipoTarifa;
    private double precio;
    private String origen;
    private String destino;
    private LocalDateTime fechaHora;

    public Boleto(String id, String nombrePasajero, String cedula, String tipoTarifa, double precio, String origen, String destino) {
        this.id = id;
        this.nombrePasajero = nombrePasajero;
        this.cedula = cedula;
        this.tipoTarifa = tipoTarifa;
        this.precio = precio;
        this.origen = origen;
        this.destino = destino;
        this.fechaHora = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getNombrePasajero() { return nombrePasajero; }
    public String getCedula() { return cedula; }
    public String getTipoTarifa() { return tipoTarifa; }
    public double getPrecio() { return precio; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    public String getInfoQR() {
        return "ID: " + id + "\nPasajero: " + nombrePasajero + "\nTarifa: " + tipoTarifa + " ($" + precio + ")\nTrayecto: " + origen + " - " + destino;
    }

    public String getDetallesFormateados() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("Boleto: %s\nFecha: %s\nPasajero: %s (%s)\nOrigen: %s\nDestino: %s\nTarifa: %s ($%.2f)", 
                id, fechaHora.format(dtf), nombrePasajero, cedula, origen, destino, tipoTarifa, precio);
    }
}