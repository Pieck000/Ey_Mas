package com.isproject.ey.firebase;

public class Evento {
    private String idEVento;
    private String idAnfitrion;
    private String nombre;
    private String ubicacion;
    private String fecha;
    private String hora;
    private String paquete;

    public Evento(){}
    public String toString()
    {
        return "Evento: "+nombre+"\n Fecha: "+fecha+"\n Hora: "+hora+"\n Paquete "+paquete+"\n";
    }

    public String getIdEVento() {
        return idEVento;
    }

    public void setIdEVento(String idEVento) {
        this.idEVento = idEVento;
    }

    public String getIdAnfitrion() {
        return idAnfitrion;
    }

    public void setIdAnfitrion(String idAnfitrion) {
        this.idAnfitrion = idAnfitrion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }
}
