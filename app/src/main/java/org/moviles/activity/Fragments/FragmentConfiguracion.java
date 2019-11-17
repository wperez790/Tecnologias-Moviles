package org.moviles.activity.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.moviles.Constants;
import org.moviles.Context;
import org.moviles.PreferencesUtils;
import org.moviles.activity.Interfaces.IFragmentConfiguracionListener;
import org.moviles.activity.R;
import org.moviles.business.ConfiguracionBusiness;
import org.moviles.model.Configuracion;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class FragmentConfiguracion extends Fragment implements AdapterView.OnItemSelectedListener{

    private TimePicker timePicker;
    private String unidad;
    private CheckBox checkBoxNotificaciones;
    private Spinner spinner;
    private IFragmentConfiguracionListener onclick ;

    public FragmentConfiguracion(IFragmentConfiguracionListener onclick){
        this.onclick = onclick;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View contenedor = inflater.inflate(R.layout.fragment_config,container,false);


        spinner = contenedor.findViewById(R.id.spinnerUnidades);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(contenedor.getContext(), R.array.unidades,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        checkBoxNotificaciones = contenedor.findViewById(R.id.actNotificaciones);
        timePicker = contenedor.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        Button btnAceptar = contenedor.findViewById(R.id.btnAceptarConfiguracion);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardarConfiguracion();
            }
        });

        cargarConfiguracion();



        return contenedor;
    }

    private void cargarConfiguracion() {

        ConfiguracionBusiness configBO = Context.getConfiguracionBusiness();
        String user = Context.getUsuarioBusiness().getCurrentUser().getUsuario();
        PreferencesUtils preferencesUtils = new PreferencesUtils(getActivity().getApplicationContext());
        Configuracion config =  configBO.getConfiguracion(user, preferencesUtils);
        unidad = config.getUnidad();
        List <String> arrayUnidades = Arrays.asList(getResources().getStringArray(R.array.unidades));
        /*Selecciono cual es el objeto del spinner para setear al cargar*/
        for(int i = 0 ; i < arrayUnidades.size() ; i++) {
            if (arrayUnidades.get(i).equals(unidad))
                spinner.setSelection(i);
        }


        checkBoxNotificaciones.setChecked(config.isNotificaciones());
        if(config.isNotificaciones()) {
            String[] aux = config.getHora().split(":");
            timePicker.setHour(Integer.parseInt(aux[0]));
            timePicker.setMinute(Integer.parseInt(aux[1]));
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unidad = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        unidad = Constants.UNIDAD_DEFAULT;
    }


    private void guardarConfiguracion(){
        Configuracion config = new Configuracion();
        config.setHora(timePicker.getHour()+":"+timePicker.getMinute());
        config.setNotificaciones(checkBoxNotificaciones.isChecked());
        config.setUnidad(unidad);
        Constants.API_UNITS = unidad.split(" ")[0];
        onclick.actualizarConfiguracion(config);


    }
}
