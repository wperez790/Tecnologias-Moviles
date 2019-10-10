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

