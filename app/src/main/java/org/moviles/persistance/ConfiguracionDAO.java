package org.moviles.persistance;

import org.json.JSONObject;
import org.moviles.Constants;
import org.moviles.Context;
import org.moviles.Util;
import org.moviles.model.Configuracion;

import java.io.File;

public class ConfiguracionDAO implements IConfiguracionDAO {
    @Override
    public Configuracion getConfiguracion(String user) {
        File userConfigFile = new File(Context.getDataDir(),
                user + "/" + Constants.USER_CONFIG);
        if(!userConfigFile.exists())
            return null;

        String json = Util.readFile(userConfigFile);

        return getFromJSON(json);
    }

    @Override
    public boolean save(Configuracion config, String user) {
        String json = getJSON(config);
        File file = new File(Context.getDataDir(),
                user+"/"+Constants.USER_CONFIG);

        return Util.writeFile(file,json);
    }

    public Configuracion getFromJSON(String jsonConfig){
        Configuracion config;
        try {
            JSONObject json = new JSONObject(jsonConfig);
            config = new Configuracion();
            config.setUnidad(json.getString("unidad"));
            config.setNotificaciones(json.getBoolean("notificacion"));
            if(config.isNotificaciones()){
                config.setHora(json.getString("hora"));
            }
            return config;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getJSON(Configuracion config){
        try{
            JSONObject json = new JSONObject();
            json.put("unidad",config.getUnidad());
            json.put("notificacion",config.isNotificaciones());
            if(config.isNotificaciones()){
                json.put("hora",config.getHora());
            }
            return json.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
