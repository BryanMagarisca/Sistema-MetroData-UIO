package utilidades;

import modelo.Pasajero;
import modelo.Usuario;

public class SesionUsuario {
    public static Object usuarioLogueado; // Puede ser Usuario (Admin) o Pasajero
    
    public static String getNombreExhibicion() {
        if (usuarioLogueado instanceof Usuario) return ((Usuario) usuarioLogueado).getNombre();
        if (usuarioLogueado instanceof Pasajero) return ((Pasajero) usuarioLogueado).getNombre();
        return "Invitado";
    }

    public static void limpiar() {
        usuarioLogueado = null;
    }
}
