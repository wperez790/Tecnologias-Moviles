package org.moviles.persistance;

import android.location.Location;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.moviles.model.Clima;

import java.util.List;

@Dao
public interface ClimaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Clima clima);

    @Query("DELETE FROM climas")
    void deleteAll();

    @Delete
    void deleteClima(Clima clima);

    @Query("SELECT * FROM climas")
    List<Clima> getClimas();

    @Query("SELECT * FROM climas where ciudad = :city order by timestamp desc limit 1")
    //@Query("SELECT * FROM climas where ciudad = :city limit 1")
    Clima getClimaByCity(String city);

    @Query("SELECT * FROM climas where coordLat = :lat and coordLon = :lon order by timestamp desc limit 1")
    Clima getClimaByLocation(Double lat, Double lon);

    @Query("SELECT * from climas where ciudad= 'Córdoba' order by timestamp desc limit :cantidad")   //Corregir 'Cordoba' para que pueda elegir.
    List<Clima> getLastClimas(Integer cantidad);
}
