package org.moviles.activity.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.moviles.activity.R;
import org.moviles.model.Clima;

import java.util.ArrayList;
import java.util.List;

public class ClimaAdapter extends RecyclerView.Adapter<ClimaAdapter.ClimaViewHolder>{

    private List<Clima> climaList;

    public ClimaAdapter() {
        climaList = new ArrayList<Clima>();

        Clima aux = new Clima();
        aux.setDia("Jueves");
        aux.setDiaNumero(5);
        aux.setMes("Septiembre");
        aux.setAnio(2019);
        aux.setCondicion("Despejado");
        aux.setHumedad(50);
        aux.setTemperatura(14.2);
        aux.setViento("NE 15Km/h ");
        aux.setDescripcion("Corre boludo a guardar el auto que cae piedra");

        climaList.add(aux);

        aux = new Clima();
        aux.setDia("Vienes");
        aux.setDiaNumero(6);
        aux.setMes("Septiembre");
        aux.setAnio(2019);
        aux.setCondicion("Despejado");
        aux.setHumedad(100);
        aux.setTemperatura(-5.7);
        aux.setViento("NE 30Km/h ");
        aux.setDescripcion("te acordaste de guardar el auto?");

        climaList.add(aux);
    }

    @Override
    public ClimaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clima_dia, parent, false);
        return new ClimaViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ClimaViewHolder holder, int position) {
        Clima aux = climaList.get(position);
        holder.getDia().setText(aux.getDia());
        holder.getFecha().setText(aux.getDiaNumero()+" de "+aux.getMes() + " de " + aux.getAnio());
        holder.getDiaCondicion().setText(aux.getCondicion());
        holder.getDiaHumedad().setText(aux.getHumedad().toString());
        holder.getDiaTemp().setText(aux.getTemperatura().toString());
        holder.getDiaViento().setText(aux.getViento());
        holder.getDiaDescripcion().setText(aux.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return climaList.size();
    }

    public class ClimaViewHolder extends RecyclerView.ViewHolder {

        private TextView dia;
        private TextView fecha;
        private TextView diaCondicion;
        private TextView diaTemp;
        private TextView diaViento;
        private TextView diaHumedad;
        private TextView diaDescripcion;


        public ClimaViewHolder(View itemView) {
            super(itemView);

            dia = itemView.findViewById(R.id.dia);
            fecha = itemView.findViewById(R.id.fecha);
            diaCondicion = itemView.findViewById(R.id.diaCondicion);
            diaTemp = itemView.findViewById(R.id.diaTemp);
            diaViento = itemView.findViewById(R.id.diaViento);
            diaHumedad = itemView.findViewById(R.id.diaHumedad);
            diaDescripcion = itemView.findViewById(R.id.diaDesc);
        }

        public TextView getDia() {
            return dia;
        }

        public TextView getFecha() {
            return fecha;
        }

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
    }
}

