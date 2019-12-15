package org.moviles.persistance;

import org.json.JSONObject;
import org.moviles.Constants;
import org.moviles.PreferencesUtils;
import org.moviles.model.Configuracion;

public class ConfiguracionDAO implements IConfiguracionDAO {
    @Override
    public Configuracion getConfiguracion(String user , PreferencesUtils preferencesUtils) {
        /*Antigua forma de obtener configuraciones
        /*File userConfigFile = new File(Contexto.getDataDir(),
                user + "/" + Constants.USER_CONFIG);
        if(!userConfigFile.exists())
            return null;

        String json = Util.readFile(userConfigFile);

        return getFromJSON(json);*/
        Configuracion configuracion = new Configuracion();
        configuracion.setUnidad(preferencesUtils.getSharedPreferences().getString(Constants.USER_UNITY,"no tiene Unidad definida"));
        configuracion.setHora(preferencesUtils.getSharedPreferences().getString(Constants.USER_TIME,"no tiene tiempo definido"));
        configuracion.setNotificaciones(preferencesUtils.getSharedPreferences().getBoolean(Constants.USER_NOTIFICATION,false));
        return configuracion;
    }

    @Override
    public boolean save(Configuracion config, String user, PreferencesUtils preferencesUtils) {
        /*Antigua Forma de guardar preferences
        /*String json = getJSON(config);
        File file = new File(Contexto.getDataDir(),
                user+"/"+Constants.USER_CONFIG);

        return Util.writeFile(file,json);*/
        try{
            preferencesUtils.setNotification(config.isNotificaciones());
            preferencesUtils.setTime(config.getHora());
            preferencesUtils.setUnity(config.getUnidad());
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }


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
