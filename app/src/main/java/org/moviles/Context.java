package org.moviles;

import org.moviles.business.UsuarioBusiness;

import java.io.File;

public class Context {
    private static UsuarioBusiness usuarioBusiness;
    private static File dataDir;

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

}
