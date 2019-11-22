package org.moviles.dto;

import org.moviles.Context;
import org.moviles.PreferencesUtils;
import org.moviles.Util;
import org.moviles.business.ConfiguracionBusiness;
import org.moviles.model.Clima;
import org.moviles.model.Configuracion;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ClimaDTO {


    List<Weather> weather;
    Main main;
    Wind wind;
    Long dt;
    String name;
    Coord coord;
    Sys sys;

    private class Sys{String country;}

    private class Coord{String lon ; String lat;}

    private class Weather {String main; String description;}

    private class Main{Double temp; Double humidity; Double temp_min; Double temp_max; String country;}

    private class Wind{ Double speed; Double deg;}

    public Clima getClima(){

        Clima clima = new Clima();
        clima.setTemperatura(main.temp);
        clima.setTempMax(main.temp_max);
        clima.setTempMin(main.temp_min);
        clima.setCondicion(weather.get(0).main);
        clima.setDescripcion(weather.get(0).description);
        clima.setVientoVelocidad(wind.speed);
        clima.setVientoDireccion(wind.deg);
        clima.setHumedad(main.humidity);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dt*1000);

        Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
        Integer anio = calendar.get(Calendar.YEAR);
        String mes = Util.getMesString(calendar.get(Calendar.MONTH)+1);
        clima.setAnio(anio);
        clima.setMes(mes);
        clima.setDiaNumero(dia);
        clima.setTimestamp(dt.longValue());
        clima.setCiudad(name);
        //clima.setCoordLat(coord.lat);
        //clima.setCoordLon(coord.lon);
        clima.setPais(sys.country);


        return clima;
    }


}
