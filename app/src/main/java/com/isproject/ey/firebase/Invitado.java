package com.isproject.ey.firebase;

public class Invitado {
    private String idInvitado;
    private String idEvnt;
    private String correo;
    private String nombreFamilia;
    private int nube;
    private int pases;
    private int Mesa;

    public Invitado(){}
    public String toString(){
        if(nube == 1){
            return "Nombre/Familia: "+nombreFamilia+"\nCorreo: "+correo+"\nPases: "+pases+"     Mesa: "+Mesa+"  Nube: Si";
        }else{
            return "Nombre/Familia: "+nombreFamilia+"\nCorreo: "+correo+"\nPases: "+pases+"     Mesa: "+Mesa+"  Nube: No";
        }
    }

    public String getIdInvitado() {
        return idInvitado;
    }

    public void setIdInvitado(String idInvitado) {
        this.idInvitado = idInvitado;
    }

    public String getIdEvnt() {
        return idEvnt;
    }

    public void setIdEvnt(String idEvnt) {
        this.idEvnt = idEvnt;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreFamilia() {
        return nombreFamilia;
    }

    public void setNombreFamilia(String nombreFamilia) {
        this.nombreFamilia = nombreFamilia;
    }

    public int getNube() {
        return nube;
    }

    public void setNube(int nube) {
        this.nube = nube;
    }

    public int getPases() {
        return pases;
    }

    public void setPases(int pases) {
        this.pases = pases;
    }

    public int getMesa() {
        return Mesa;
    }

    public void setMesa(int mesa) {
        Mesa = mesa;
    }
}
