package org.moviles.activity.Fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import org.moviles.Context;
import org.moviles.PreferencesUtils;
import org.moviles.activity.R;
import org.moviles.business.ConfiguracionBusiness;
import org.moviles.model.Clima;
import org.moviles.model.Configuracion;

import java.util.List;


public class FragmentHome extends Fragment {

    private Clima clima;
    private TextView temperaturaTextView;
    private TextView tempMaxTextView;
    private TextView tempMinTextView;
    private TextView vientoTextView;
    private TextView humedadTextView;
    private TextView descripcionBTextView;
    private TextView descripcionDTextView;
    private ImageView imageWeather;
    ConfiguracionBusiness configuracionBusiness;
    String unidadViento;
    String unidadTemp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contenedor = inflater.inflate(R.layout.fragment_home,container,false);
        Clima clima  = Context.getClima();

        configuracionBusiness = Context.getConfiguracionBusiness();
        PreferencesUtils pu = new PreferencesUtils(Context.getContext());
        Configuracion config = configuracionBusiness.getConfiguracion(Context.getUsuarioBusiness().getCurrentUser().getUsuario(), pu);
        String [] units= config.getUnidad().split(" ");
        defineUnits(units[0]);

        temperaturaTextView = contenedor.findViewById(R.id.textoTemperatura);
        temperaturaTextView.setText(clima.getTemperatura().toString() + unidadTemp);
        tempMaxTextView = contenedor.findViewById(R.id.textoTemperaturaMax);
        tempMaxTextView.setText(getString(R.string.temperaturaMaxima) +" "+ clima.getTempMax().toString() + unidadTemp);
        tempMinTextView = contenedor.findViewById(R.id.textoTemperaturaMin);
        tempMinTextView.setText(getString(R.string.temperaturaMinima) +" "+ clima.getTempMin().toString()+ unidadTemp);
        vientoTextView = contenedor.findViewById(R.id.vientoVelocidad);
        vientoTextView.setText(clima.getVientoVelocidad().toString()+ unidadViento);
        humedadTextView= contenedor.findViewById(R.id.humedad);
        humedadTextView.setText(clima.getHumedad().toString() + " %");
        descripcionBTextView= contenedor.findViewById(R.id.descripcionBreve);
        descripcionBTextView.setText(clima.getDescripcion());
        descripcionDTextView= contenedor.findViewById(R.id.descripcionDetallada);
        descripcionDTextView.setText(clima.getCondicion());
        imageWeather = contenedor.findViewById(R.id.imageWeather);
        imageWeather.setImageDrawable(getImageByDescription(clima.getCondicion()));


        return contenedor;
    }

    private Drawable getImageByDescription(String descripcion) {
        Drawable draw = getResources().getDrawable(R.drawable.ic_map, getContext().getTheme());
        switch (descripcion){
            case "Thunderstorm": draw = getResources().getDrawable(R.drawable.ic_thunder, getContext().getTheme());
                break;
            case "Clear": draw = getResources().getDrawable(R.drawable.ic_day, getContext().getTheme());
                break;
            case "Clouds" : draw =  getResources().getDrawable(R.drawable.ic_cloudy, getContext().getTheme());
                break;
            case "Rain": draw = getResources().getDrawable(R.drawable.ic_rainy_6, getContext().getTheme());
                break;
            case "Drizzle": draw =  getResources().getDrawable(R.drawable.ic_rainy_2, getContext().getTheme());
                break;
                
        }
        return draw;

    }

    private void defineUnits(String unidad) {
        if(unidad.equals("metric") || unidad.equals("ºC")){
            unidadTemp=" ºC";
            unidadViento=" Km/h";
        }
        else if(unidad.equals("imperial") || unidad.equals("ºF")){
            unidadTemp=" ºF";
            unidadViento=" Mi/h";
        }
        else{
            unidadTemp = " ºK";
            unidadViento = " Km/h";
        }
    }


}
