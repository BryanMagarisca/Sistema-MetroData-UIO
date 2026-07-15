package modelo;

import estructuras.Pila;
import java.util.ArrayList;
import java.util.List;

public class Cochera {
    private String nombre;
    private Pila<Tren> pilaTrenes;
    private List<Tren> trenesEnCirculacion;
    private String ultimaOperacion = "Ninguna";

    public Cochera(String nombre) {
        this.nombre = nombre;
        this.pilaTrenes = new Pila<>();
        this.trenesEnCirculacion = new ArrayList<>();
    }

    public void agregarTren(Tren tren) {
        pilaTrenes.push(tren);
        tren.setEstado("EN_COCHERA");
        ultimaOperacion = "Ingresó " + tren.getId();
    }

    public Tren despacharTren(String proximoId) {
        if (!pilaTrenes.estaVacia()) {
            Tren t = pilaTrenes.pop();
            t.setEstado("EN_ESTACION");
            trenesEnCirculacion.add(t);
            ultimaOperacion = "Despachó " + t.getId();
            return t;
        }
        return null;
    }

    public void recibirTren(Tren tren) {
        trenesEnCirculacion.remove(tren);
        pilaTrenes.push(tren);
        tren.setEstado("EN_COCHERA");
        ultimaOperacion = "Ingresó " + tren.getId();
    }

    public Pila<Tren> getPilaTrenes() { return pilaTrenes; }
    public int getCantidadEnCochera() { return pilaTrenes.getTamaño(); }
    public int getCantidadCirculando() { return trenesEnCirculacion.size(); }
    public String getNombre() { return nombre; }
    public String getUltimaOperacion() { return ultimaOperacion; }
}