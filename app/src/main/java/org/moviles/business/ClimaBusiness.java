package org.moviles.business;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.moviles.Context;
import org.moviles.model.Clima;
import org.moviles.persistance.ClimaRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ClimaBusiness {
    private ClimaRepository climaRepository ;

    private Location loc;

    public ClimaBusiness(Application application){
        climaRepository = new ClimaRepository(application);

    }

    public void insert(Clima clima){
        try{
            climaRepository.insert(clima);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void insertClimas(List<Clima> climasList){
        try{
            climaRepository.insertAllClimas(climasList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Clima> getAll(){
        return climaRepository.getAllClimas();

    }

    public Clima getClimaByCity(String city){
        Clima climaByCity = climaRepository.getClimaByCity(city);
        return climaByCity;
    }



    public Clima getClimaByLocation(Location location) {

        return climaRepository.getClimaByLocation(location);
    }

    public List<Clima> getLastDays(Integer cantidad){
        return climaRepository.getLastClimas(cantidad);
    }
}
