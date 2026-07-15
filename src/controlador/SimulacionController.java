package controlador;

import estructuras.ListaDobleEnlazada.Nodo;
import modelo.Estacion;
import modelo.Tren;
import utilidades.RelojSistema;
import java.util.ArrayList;
import java.util.List;

public class SimulacionController {
    private RutaController rutaController;
    private List<Tren> trenesEnVía;
    private int viajesCompletados = 0;
    
    public static final int TIEMPO_POR_ESTACION = 4; 
    public static final int FRECUENCIA_DESPACHO = 8; 
    private int ultimoDespachoMinuto = -1;

    public SimulacionController(RutaController rutaController) {
        this.rutaController = rutaController;
        this.trenesEnVía = new ArrayList<>();
        RelojSistema.getInstancia().agregarSuscriptor(e -> actualizar());
    }

    private void actualizar() {
        int minutoActual = RelojSistema.getInstancia().getTiempoTotalMinutos();
        
        if (minutoActual % FRECUENCIA_DESPACHO == 0 && minutoActual != ultimoDespachoMinuto) {
            despacharTrenes();
            ultimoDespachoMinuto = minutoActual;
        }

        List<Tren> aEliminar = new ArrayList<>();
        for (Tren t : trenesEnVía) {
            avanzarTren(t);
            if (t.getEstado().equals("EN_COCHERA")) aEliminar.add(t);
        }
        
        for (Tren t : aEliminar) {
            trenesEnVía.remove(t);
            t.getEstacionActual().dato.getCochera().recibirTren(t);
            viajesCompletados++;
        }
    }

    private void despacharTrenes() {
        Estacion labrador = rutaController.getEstacionLabrador();
        Estacion quitumbe = rutaController.getEstacionQuitumbe();
        
        Tren tNS = labrador.getCochera().despacharTren("ID");
        if (tNS != null) prepararTrenParaSalida(tNS, true);

        Tren tSN = quitumbe.getCochera().despacharTren("ID");
        if (tSN != null) prepararTrenParaSalida(tSN, false);
    }
    
    private void prepararTrenParaSalida(Tren t, boolean norteSur) {
        t.setEstado("EN_TUNEL");
        t.setProgreso(0.0);
        t.setDireccionNorteSur(norteSur);
        if (norteSur) {
            t.setEstacionActual(rutaController.getLinea1().getCabeza());
            t.setProximaEstacion(rutaController.getLinea1().getCabeza().siguiente);
        } else {
            t.setEstacionActual(rutaController.getLinea1().getCola());
            t.setProximaEstacion(rutaController.getLinea1().getCola().anterior);
        }
        trenesEnVía.add(t);
    }

    private void avanzarTren(Tren t) {
        double incremento = 1.0 / (double) TIEMPO_POR_ESTACION;
        t.setProgreso(t.getProgreso() + incremento);
        
        if (t.getProgreso() >= 0.99) {
            t.setEstacionActual(t.getProximaEstacion());
            t.setProgreso(0.0);
            
            Nodo<Estacion> siguiente = t.isDireccionNorteSur() ? t.getEstacionActual().siguiente : t.getEstacionActual().anterior;
            if (siguiente == null) {
                t.setEstado("EN_COCHERA");
            } else {
                t.setProximaEstacion(siguiente);
            }
        }
    }

    public int calcularTiempoLlegada(Estacion origen, boolean direccionNS) {
        // CORRECCIÓN: Si el usuario está en la estación de salida (Cabecera para NS, Cola para SN)
        // debemos considerar el tiempo hasta el próximo despacho si no hay trenes en vía.
        
        Tren masCercano = null;
        double minGaps = Double.MAX_VALUE;
        
        for (Tren t : trenesEnVía) {
            if (t.isDireccionNorteSur() == direccionNS) {
                double distancia = calcularDistancia(t, origen, direccionNS);
                if (distancia >= 0 && distancia < minGaps) {
                    minGaps = distancia;
                    masCercano = t;
                }
            }
        }

        if (masCercano != null) {
            return (int) Math.round(minGaps * (double) TIEMPO_POR_ESTACION);
        }

        // Si no hay trenes en vía, calculamos el tiempo para el próximo despacho desde la cochera correspondiente
        int minutoActual = RelojSistema.getInstancia().getTiempoTotalMinutos();
        int minutosParaDespacho = FRECUENCIA_DESPACHO - (minutoActual % FRECUENCIA_DESPACHO);
        
        // El tiempo de llegada será: minutos para que salga + tiempo de viaje desde la cabecera a la estación del usuario
        int idxOrigen = obtenerIndice(origen);
        int estacionesDeDistancia = direccionNS ? idxOrigen : (rutaController.getLinea1().getTamaño() - 1 - idxOrigen);
        
        return minutosParaDespacho + (estacionesDeDistancia * TIEMPO_POR_ESTACION);
    }

    private double calcularDistancia(Tren t, Estacion destino, boolean ns) {
        int idxTren = obtenerIndice(t.getEstacionActual().dato);
        int idxDestino = obtenerIndice(destino);
        
        if (ns) {
            if (idxTren > idxDestino) return -1;
            return (double)(idxDestino - idxTren) - t.getProgreso();
        } else {
            if (idxTren < idxDestino) return -1;
            return (double)(idxTren - idxDestino) - t.getProgreso();
        }
    }

    private int obtenerIndice(Estacion e) {
        int i = 0;
        for (Estacion est : rutaController.getLinea1()) {
            if (est.getNombre().equals(e.getNombre())) return i;
            i++;
        }
        return -1;
    }

    public List<Tren> getTrenesActivos() { return trenesEnVía; }
    public int getViajesCompletados() { return viajesCompletados; }
}