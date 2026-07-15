package modelo;

public class Estacion {
    private String nombre;
    private Cochera cochera; // Una estación puede o no tener una cochera física

    public Estacion(String nombre) {
        this.nombre = nombre;
    }

    public Estacion(String nombre, boolean tieneCochera) {
        this.nombre = nombre;
        if (tieneCochera) {
            this.cochera = new Cochera("Cochera " + nombre);
        }
    }

    public String getNombre() { return nombre; }
    public boolean tieneCochera() { return cochera != null; }
    public Cochera getCochera() { return cochera; }

    @Override
    public String toString() { return nombre; }
}
