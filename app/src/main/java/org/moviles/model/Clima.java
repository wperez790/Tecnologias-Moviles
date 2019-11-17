package org.moviles.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "climas")
public class Clima{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_clima")
    private int id;

    private String dia;
    private Integer diaNumero;
    private String mes;
    private Integer anio;
    private String descripcion;
    private Double temperatura;
    private Double tempMax;
    private Double tempMin;
    private Double humedad;
    private String condicion;
    private Double vientoVelocidad;
    private Double vientoDireccion;


    public Double getVientoVelocidad() {
        return vientoVelocidad;
    }

    public void setVientoVelocidad(Double vientoVelocidad) {
        this.vientoVelocidad = vientoVelocidad;
    }

    public Double getVientoDireccion() {
        return vientoDireccion;
    }

    public void setVientoDireccion(Double vientoDireccion) {
        this.vientoDireccion = vientoDireccion;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getHumedad() {
        return humedad;
    }

    public void setHumedad(Double humedad) {
        this.humedad = humedad;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Integer getDiaNumero() {
        return diaNumero;
    }

    public void setDiaNumero(Integer diaNumero) {
        this.diaNumero = diaNumero;
    }
}
