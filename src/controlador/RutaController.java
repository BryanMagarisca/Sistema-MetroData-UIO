package controlador;

import estructuras.ListaDobleEnlazada;
import modelo.Estacion;
import modelo.Tren;

public class RutaController {
    private ListaDobleEnlazada<Estacion> linea1;

    public RutaController() {
        linea1 = new ListaDobleEnlazada<>();
        inicializarLinea();
        poblarCocheras();
    }

    private void inicializarLinea() {
        linea1.agregarAlFinal(new Estacion("El Labrador", true));
        linea1.agregarAlFinal(new Estacion("Jipijapa"));
        linea1.agregarAlFinal(new Estacion("Iñaquito"));
        linea1.agregarAlFinal(new Estacion("La Carolina"));
        linea1.agregarAlFinal(new Estacion("La Pradera"));
        linea1.agregarAlFinal(new Estacion("Universidad Central"));
        linea1.agregarAlFinal(new Estacion("El Ejido"));
        linea1.agregarAlFinal(new Estacion("La Alameda"));
        linea1.agregarAlFinal(new Estacion("San Francisco"));
        linea1.agregarAlFinal(new Estacion("La Magdalena"));
        linea1.agregarAlFinal(new Estacion("El Recreo"));
        linea1.agregarAlFinal(new Estacion("Cardenal de la Torre"));
        linea1.agregarAlFinal(new Estacion("Solanda"));
        linea1.agregarAlFinal(new Estacion("Morán Valverde"));
        linea1.agregarAlFinal(new Estacion("Quitumbe", true));
    }

    private void poblarCocheras() {
        // Asignamos 10 trenes a cada cochera al inicio
        Estacion labrador = linea1.getCabeza().dato;
        Estacion quitumbe = linea1.getCola().dato;

        for (int i = 1; i <= 10; i++) {
            labrador.getCochera().agregarTren(new Tren("T-L" + i, linea1.getCabeza(), true));
            quitumbe.getCochera().agregarTren(new Tren("T-Q" + i, linea1.getCola(), false));
        }
    }

    public ListaDobleEnlazada<Estacion> getLinea1() { return linea1; }
    public Estacion getEstacionLabrador() { return linea1.getCabeza().dato; }
    public Estacion getEstacionQuitumbe() { return linea1.getCola().dato; }
}
