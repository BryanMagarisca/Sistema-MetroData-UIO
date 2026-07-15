package modelo;

import estructuras.ListaDobleEnlazada.Nodo;

public class Tren {
    private String id;
    private Nodo<Estacion> estacionActual;
    private Nodo<Estacion> proximaEstacion;
    private double progreso;
    private boolean direccionNorteSur;
    private String estado;

    public Tren(String id, Nodo<Estacion> inicio, boolean direccionNorteSur) {
        this.id = id;
        this.estacionActual = inicio;
        this.direccionNorteSur = direccionNorteSur;
        this.progreso = 0;
        this.estado = "EN_COCHERA";
    }

    public String getId() { return id; }
    public Nodo<Estacion> getEstacionActual() { return estacionActual; }
    public void setEstacionActual(Nodo<Estacion> estacion) { this.estacionActual = estacion; }
    public Nodo<Estacion> getProximaEstacion() { return proximaEstacion; }
    public void setProximaEstacion(Nodo<Estacion> proxima) { this.proximaEstacion = proxima; }
    public double getProgreso() { return progreso; }
    public void setProgreso(double progreso) { this.progreso = progreso; }
    public boolean isDireccionNorteSur() { return direccionNorteSur; }
    public void setDireccionNorteSur(boolean dir) { this.direccionNorteSur = dir; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
