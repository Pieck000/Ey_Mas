package com.isproject.ey.firebase;

public class Proovedor {
    private String idProv;
    private String idEv;
    private String nombre;
    private String correo;
    private int personal;
    private String telefono;

    public Proovedor(){}

    public String toString()
    {
        return "Proovedor: "+nombre+"\n Correo: "+correo+"\n Personas: "+personal+"\n";
    }

    public String getIdEv() {
        return idEv;
    }

    public void setIdEv(String idEv) {
        this.idEv = idEv;
    }

    public String getIdProv() {
        return idProv;
    }

    public void setIdProv(String idProv) {
        this.idProv = idProv;
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

    public int getPersonal() {
        return personal;
    }

    public void setPersonal(int personal) {
        this.personal = personal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
