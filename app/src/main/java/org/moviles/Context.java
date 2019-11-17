package org.moviles;

import org.moviles.business.ConfiguracionBusiness;
import org.moviles.business.UsuarioBusiness;
import org.moviles.model.Clima;

import java.io.File;

public class Context {
    private static UsuarioBusiness usuarioBusiness;
    private static File dataDir;
    private static ConfiguracionBusiness configuracionBusiness;
    private static android.content.Context context;
    public static Clima clima;

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
}
