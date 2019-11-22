package org.moviles.persistance;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import org.moviles.Constants;
import org.moviles.model.Clima;

@Database(entities = {Clima.class}, version = Constants.VERSION_DATABASE_CLIMA)
public abstract class ClimaRoomDatabase extends RoomDatabase {

    public abstract ClimaDAO climaDAO();

    private static volatile ClimaRoomDatabase INSTANCE;

    /*SINGLETON*/
    static ClimaRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (ClimaRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ClimaRoomDatabase.class,"ClimaRoomDatabase").build();
                }
            }
        }
        return INSTANCE;
    }
}
