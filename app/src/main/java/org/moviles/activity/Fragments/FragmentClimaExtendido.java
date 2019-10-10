package org.moviles.activity.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.moviles.activity.Adapters.ClimaAdapter;
import org.moviles.activity.R;
import org.moviles.model.Clima;

import java.util.ArrayList;
import java.util.List;

public class FragmentClimaExtendido extends Fragment {

    List<Clima> climaList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clima_extendido,container,false);
        cargarLista();
        
        RecyclerView recyclerView = view.findViewById(R.id.contenedorRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ClimaAdapter(climaList));
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    private void cargarLista() {

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
        aux.setDescripcion("Cielo despejado, baja probabilidad de lluvias, vientos leves");

        climaList.add(aux);

        aux = new Clima();
        aux.setDia("Vienes");
        aux.setDiaNumero(6);
        aux.setMes("Septiembre");
        aux.setAnio(2019);
        aux.setCondicion("Nublado");
        aux.setHumedad(100);
        aux.setTemperatura(-5.7);
        aux.setViento("NE 30Km/h ");
        aux.setDescripcion("Cielo nublado, alta probabilidad de lluvias, vientos leves a moderados");

        climaList.add(aux);

    }
}
