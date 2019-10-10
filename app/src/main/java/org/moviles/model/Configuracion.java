package org.moviles.model;

import org.moviles.Constants;

public class Configuracion {

    private String unidad;
    private boolean notificaciones;
    private String hora;

    public Configuracion() {
        this.unidad = Constants.UNIDAD_DEFAULT;
        this.notificaciones = false;
        this.hora = "00:00";
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public boolean isNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(boolean notificaciones) {
        this.notificaciones = notificaciones;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
