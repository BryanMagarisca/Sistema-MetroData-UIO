package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class Pasajero implements Comparable<Pasajero>, Serializable {
    private static final long serialVersionUID = 1L;
    private String cedula;
    private String nombre;
    private String apellido;
    private String telefono;
    private String domicilio;
    private LocalDate fechaNacimiento;
    private String correo;
    private String password;

    public Pasajero(String cedula, String nombre, String apellido, String telefono, String domicilio, LocalDate fechaNacimiento, String correo, String password) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.password = password;
    }

    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String getTipoTarifa() {
        int edad = getEdad();
        return (edad < 18 || edad >= 65) ? "Reducida" : "Normal";
    }

    public double getPrecioTarifa() {
        return getTipoTarifa().equals("Normal") ? 0.45 : 0.22;
    }

    // Getters
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getDomicilio() { return domicilio; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }

    // Setters para permitir la modificación de datos
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public int compareTo(Pasajero otro) {
        return this.cedula.compareTo(otro.cedula);
    }
}