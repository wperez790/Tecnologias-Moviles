package org.moviles.activity.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.moviles.Contexto;
import org.moviles.PreferencesUtils;
import org.moviles.activity.R;
import org.moviles.business.ConfiguracionBusiness;
import org.moviles.model.Clima;
import org.moviles.model.Configuracion;

import java.util.ArrayList;
import java.util.List;

public class ClimaAdapter extends RecyclerView.Adapter<ClimaAdapter.ClimaViewHolder>{

    private List<Clima> climaList;
    private String unidadTemp;
    private String unidadViento;

    public ClimaAdapter( List<Clima>climaList) {
        this.climaList = climaList;
    }


    public List<Clima> getClimaList() {
        return climaList;
    }

    @Override
    public ClimaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clima_dia, parent, false);
        return new ClimaViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ClimaViewHolder holder, int position) {
        Clima aux = climaList.get(position);
        //holder.getDia().setText(aux.getDia());
        ConfiguracionBusiness configuracionBusiness;
        configuracionBusiness = Contexto.getConfiguracionBusiness();
        PreferencesUtils pu = new PreferencesUtils(Contexto.getContext());
        Configuracion config = configuracionBusiness.getConfiguracion(Contexto.getUsuarioBusiness().getCurrentUser().getUsuario(), pu);
        String [] units= config.getUnidad().split(" ");
        defineUnits(units[0]);
        holder.getFecha().setText(aux.getDiaNumero()+" de "+aux.getMes() + " de " + aux.getAnio());
        holder.getHora().setText(aux.getHora());
        holder.getDiaCondicion().setText(aux.getCondicion());
        holder.getDiaHumedad().setText(aux.getHumedad().toString() + " %");
        holder.getDiaTemp().setText(aux.getTemperatura().toString() + unidadTemp);
        holder.getDiaViento().setText(aux.getVientoVelocidad().toString() + unidadViento);
        holder.getDiaDescripcion().setText(aux.getDescripcion());
        holder.getImgCondicion().setImageDrawable(getDrawableByCondicion(aux.getCondicion()));
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



    private Drawable getDrawableByCondicion(String condicion) {
        Drawable draw = Contexto.getContext().getResources().getDrawable(R.drawable.ic_map, Contexto.getContext().getTheme());
        switch (condicion){
            case "Thunderstorm": draw = Contexto.getContext().getResources().getDrawable(R.drawable.ic_thunder, Contexto.getContext().getTheme());
                break;
            case "Clear": draw = Contexto.getContext().getResources().getDrawable(R.drawable.ic_day, Contexto.getContext().getTheme());
                break;
            case "Clouds" : draw =  Contexto.getContext().getResources().getDrawable(R.drawable.ic_cloudy, Contexto.getContext().getTheme());
                break;
            case "Rain": draw = Contexto.getContext().getResources().getDrawable(R.drawable.ic_rainy_6, Contexto.getContext().getTheme());
                break;
            case "Drizzle": draw =  Contexto.getContext().getResources().getDrawable(R.drawable.ic_rainy_2, Contexto.getContext().getTheme());
                break;

        }

        return draw;

    }

    @Override
    public int getItemCount() {
        return climaList.size();
    }

    public class ClimaViewHolder extends RecyclerView.ViewHolder {

        private TextView dia;
        private TextView fecha;
        private TextView hora;
        private TextView diaCondicion;
        private TextView diaTemp;
        private TextView diaViento;
        private TextView diaHumedad;
        private TextView diaDescripcion;
        private ImageView imgCondicion;


        public ClimaViewHolder(View itemView) {
            super(itemView);

            //dia = itemView.findViewById(R.id.dia);
            fecha = itemView.findViewById(R.id.fecha);
            hora =  itemView.findViewById(R.id.hora);
            diaCondicion = itemView.findViewById(R.id.diaCondicion);
            diaTemp = itemView.findViewById(R.id.diaTemp);
            diaViento = itemView.findViewById(R.id.diaViento);
            diaHumedad = itemView.findViewById(R.id.diaHumedad);
            diaDescripcion = itemView.findViewById(R.id.diaDesc);
            imgCondicion = itemView.findViewById(R.id.diaImg);
        }

       /* public TextView getDia() {
            return dia;
        }*/

        public TextView getFecha() {
            return fecha;
        }

        public TextView getHora() {  return hora; }

        public TextView getDiaCondicion() {
            return diaCondicion;
        }

        public TextView getDiaTemp() {
            return diaTemp;
        }

        public TextView getDiaViento() {
            return diaViento;
        }

        public TextView getDiaHumedad() {
            return diaHumedad;
        }

        public TextView getDiaDescripcion() {
            return diaDescripcion;
        }

        public ImageView getImgCondicion() {
            return imgCondicion;
        }
    }
}

