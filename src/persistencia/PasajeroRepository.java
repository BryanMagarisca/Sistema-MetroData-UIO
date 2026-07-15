package persistencia;

import estructuras.ArbolBinarioBusqueda;
import modelo.Pasajero;
import utilidades.PersistenciaUtil;

public class PasajeroRepository {
    private static final String FILE_PATH = "pasajeros.dat";
    private static PasajeroRepository instancia;
    private ArbolBinarioBusqueda<Pasajero> arbolPasajeros;

    private PasajeroRepository() {
        cargar();
    }

    public static PasajeroRepository getInstancia() {
        if (instancia == null) instancia = new PasajeroRepository();
        return instancia;
    }

    @SuppressWarnings("unchecked")
    public void cargar() {
        Object data = PersistenciaUtil.cargar(FILE_PATH);
        if (data instanceof ArbolBinarioBusqueda) {
            this.arbolPasajeros = (ArbolBinarioBusqueda<Pasajero>) data;
        } else {
            this.arbolPasajeros = new ArbolBinarioBusqueda<>();
        }
    }

    public void guardar() {
        PersistenciaUtil.guardar(arbolPasajeros, FILE_PATH);
    }

    public ArbolBinarioBusqueda<Pasajero> getArbolPasajeros() {
        if (arbolPasajeros == null) cargar();
        return arbolPasajeros;
    }
}
