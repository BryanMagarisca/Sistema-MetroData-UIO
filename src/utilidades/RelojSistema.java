package utilidades;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RelojSistema {
    private static RelojSistema instancia;
    private int horas;
    private int minutos;
    private int segundos;
    private Timer timer;
    private List<ActionListener> suscriptores;

    // Velocidad de simulación: 1 segundo real = 1 minuto simulado (ajustable)
    private static final int DELAY_MS = 1000; 

    private RelojSistema() {
        this.horas = 5; // El metro inicia operaciones a las 05:00
        this.minutos = 55;
        this.segundos = 0;
        this.suscriptores = new ArrayList<>();
        
        this.timer = new Timer(DELAY_MS, e -> {
            avanzarMinuto();
            notificarSuscriptores();
        });
    }

    public static RelojSistema getInstancia() {
        if (instancia == null) instancia = new RelojSistema();
        return instancia;
    }

    public void iniciar() { timer.start(); }
    public void detener() { timer.stop(); }

    private void avanzarMinuto() {
        minutos++;
        if (minutos >= 60) {
            minutos = 0;
            horas++;
            if (horas >= 24) horas = 0;
        }
    }

    public void agregarSuscriptor(ActionListener al) {
        suscriptores.add(al);
    }

    private void notificarSuscriptores() {
        for (ActionListener al : suscriptores) {
            al.actionPerformed(null);
        }
    }

    public String getTiempoFormateado() {
        return String.format("%02d:%02d", horas, minutos);
    }

    public int getHoras() { return horas; }
    public int getMinutos() { return minutos; }
    public int getTiempoTotalMinutos() { return (horas * 60) + minutos; }
}
