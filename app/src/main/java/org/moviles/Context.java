package org.moviles;

import android.app.Application;

import org.moviles.business.ClimaBusiness;
import org.moviles.business.ConfiguracionBusiness;
import org.moviles.business.UsuarioBusiness;
import org.moviles.model.Clima;
import org.moviles.persistance.ClimaRepository;
import org.moviles.persistance.ClimaRoomDatabase;

import java.io.File;
import java.util.List;

public class Context {
    public static List<Clima> climaList;
    private static UsuarioBusiness usuarioBusiness;
    private static File dataDir;
    private static ConfiguracionBusiness configuracionBusiness;
    private static android.content.Context context;
    public static Clima clima;
    private static ClimaBusiness climaBusiness;
    public static void setDataDir(File dir){
        dataDir = dir;
    }

    public static File getDataDir(){
        return dataDir;
    }

    public static UsuarioBusiness getUsuarioBusiness(){
        if (usuarioBusiness == null)
            usuarioBusiness = new UsuarioBusiness();

        return usuarioBusiness;
    }

    public static ConfiguracionBusiness getConfiguracionBusiness(){
        if (configuracionBusiness== null)
            configuracionBusiness= new ConfiguracionBusiness();

        return configuracionBusiness;
    }
    public static void setContext(android.content.Context c){
        context = c;
    }

    public static android.content.Context getContext() {
        return context;
    }

    public static Clima getClima() {
        return clima;
    }
    public static void setClima(Clima climaToSet){ clima = climaToSet;}

    public static ClimaBusiness getClimaBusiness(Application application) {
        if (climaBusiness == null)
            climaBusiness = new ClimaBusiness(application);
        return climaBusiness;

    }

    public static List<Clima> getClimaList() {
        return climaList;
    }

    public static void setClimaList(List<Clima> climaList) {
        Context.climaList = climaList;
    }
}
