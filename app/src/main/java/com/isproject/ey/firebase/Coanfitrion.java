package com.isproject.ey.firebase;

public class Coanfitrion {
    private String idCo;
    private String idEv;
    private String nombre;
    private String correo;

    public Coanfitrion(){}
    public String toString()
    {
        return nombre+" \n"+correo;
    }

    public String getIdEv() {
        return idEv;
    }

    public void setIdEv(String idEv) {
        this.idEv = idEv;
    }

    public String getIdCo() {
        return idCo;
    }

    public void setIdCo(String idCo) {
        this.idCo = idCo;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
