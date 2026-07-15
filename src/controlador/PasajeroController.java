package controlador;

import estructuras.ArbolBinarioBusqueda;
import modelo.Pasajero;
import persistencia.PasajeroRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PasajeroController {
    
    public PasajeroController() {}

    private PasajeroRepository getRepo() {
        return PasajeroRepository.getInstancia();
    }

    public boolean registrarPasajeroFull(String cedula, String nombre, String apellido, String telefono, String domicilio, LocalDate fechaNac, String correo, String password) {
        Pasajero existente = buscarPasajero(cedula);
        if (existente != null) {
            // Si existe, actualizamos todos los campos (Modo edición)
            existente.setNombre(nombre);
            existente.setApellido(apellido);
            existente.setTelefono(telefono);
            existente.setDomicilio(domicilio);
            existente.setFechaNacimiento(fechaNac);
            existente.setCorreo(correo);
            existente.setPassword(password);
            getRepo().guardar();
            return true;
        }
        
        // Si no existe, creamos uno nuevo
        Pasajero nuevo = new Pasajero(cedula, nombre, apellido, telefono, domicilio, fechaNac, correo, password);
        getRepo().getArbolPasajeros().insertar(nuevo);
        getRepo().guardar();
        return true;
    }

    public void actualizarDomicilio(String cedula, String nuevoDomicilio) {
        Pasajero p = buscarPasajero(cedula);
        if (p != null) {
            p.setDomicilio(nuevoDomicilio);
            getRepo().guardar();
        }
    }

    public Pasajero buscarPasajero(String cedula) {
        Pasajero dummy = new Pasajero(cedula, "", "", "", "", null, "", "");
        return getRepo().getArbolPasajeros().buscar(dummy);
    }

    public List<Pasajero> listarPasajerosInorden() {
        List<Pasajero> lista = new ArrayList<>();
        getRepo().getArbolPasajeros().inorden(lista::add);
        return lista;
    }

    public boolean eliminarPasajero(String cedula) {
        Pasajero p = buscarPasajero(cedula);
        if (p != null) {
            getRepo().getArbolPasajeros().eliminar(p);
            getRepo().guardar();
            return true;
        }
        return false;
    }

    public int getTotalPasajeros() {
        return listarPasajerosInorden().size();
    }
}