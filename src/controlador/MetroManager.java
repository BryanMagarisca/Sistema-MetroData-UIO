package controlador;

import modelo.Boleto;
import persistencia.BoletoRepository;
import utilidades.RelojSistema;
import java.util.List;

public class MetroManager {
    private static MetroManager instancia;
    private RutaController rutaController;
    private SimulacionController simulacionController;

    private MetroManager() {
        rutaController = new RutaController();
        simulacionController = new SimulacionController(rutaController);
        // Iniciamos el reloj al crear el manager
        RelojSistema.getInstancia().iniciar();
    }

    public static MetroManager getInstancia() {
        if (instancia == null) instancia = new MetroManager();
        return instancia;
    }

    public RutaController getRutaController() { return rutaController; }
    public SimulacionController getSimulacionController() { return simulacionController; }
    
    public void registrarVenta(Boleto b) { 
        BoletoRepository.getInstancia().guardarBoleto(b);
    }
    
    public List<Boleto> getVentasRealizadas() { 
        return BoletoRepository.getInstancia().getBoletosOrdenados();
    }
}