package com.isproject.ey.firebase;

public class Usuario {
    private String idUsu;
    private String nombre;
    private String apellidos;
    private String fecha;
    private String tel;
    private String correo;
    private String password;

    public Usuario(){}
    public String toString()
    {
        return "Nombre: "+nombre+"\n Correo"+correo+"\n";
    }
    public String getIdUsu() {
        return idUsu;
    }
    public void setIdUsu(String idUsu) {
        this.idUsu = idUsu;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
