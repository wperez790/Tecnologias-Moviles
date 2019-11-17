package org.moviles.activity.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import org.moviles.Context;
import org.moviles.activity.R;
import org.moviles.model.Clima;

import java.util.List;


public class FragmentHome extends Fragment {

    private Clima clima;
    private TextView temperaturaTextView;
    private TextView vientoTextView;
    private TextView humedadTextView;
    private TextView descripcionBTextView;
    private TextView descripcionDTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contenedor = inflater.inflate(R.layout.fragment_home,container,false);
        Clima clima  = Context.getClima();
        temperaturaTextView = contenedor.findViewById(R.id.textoTemperatura);
        temperaturaTextView.setText(clima.getTemperatura().toString());
        vientoTextView = contenedor.findViewById(R.id.vientoVelocidad);
        vientoTextView.setText(clima.getVientoVelocidad().toString());
        humedadTextView= contenedor.findViewById(R.id.humedad);
        humedadTextView.setText(clima.getHumedad().toString());
        descripcionBTextView= contenedor.findViewById(R.id.descripcionBreve);
        descripcionBTextView.setText(clima.getDescripcion());
        descripcionDTextView= contenedor.findViewById(R.id.descripcionDetallada);
        descripcionDTextView.setText(clima.getCondicion());


        return contenedor;
    }



}
