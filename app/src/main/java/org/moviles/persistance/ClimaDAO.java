package org.moviles.persistance;

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


}
