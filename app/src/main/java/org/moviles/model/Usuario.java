package org.moviles.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

import java.util.Date;

//@Entity(tableName = "users")
public class Usuario{

    //@PrimaryKey
    //@NonNull
   // @ColumnInfo(name = "user_name")
    private String usuario;
    private String password;
    private String email;
    //@ColumnInfo(name = "birth_day")
    private Date fechaNacimiento;
    //@ColumnInfo(name = "perfil_img")
    private Bitmap fotoPerfil;


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Bitmap getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(Bitmap fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
