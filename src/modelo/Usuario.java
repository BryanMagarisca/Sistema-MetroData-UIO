package modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;
    private String username;
    private String password;
    private String rol; // SUPER_ADMIN, ADMIN, USUARIO

    public Usuario(String nombre, String username, String password, String rol) {
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRol() { return rol; }
}
