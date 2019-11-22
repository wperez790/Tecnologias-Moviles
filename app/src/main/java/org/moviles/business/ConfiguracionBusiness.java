package org.moviles.business;

import android.content.Context;

import org.moviles.PreferencesUtils;
import org.moviles.model.Configuracion;
import org.moviles.persistance.ConfiguracionDAO;
import org.moviles.persistance.IConfiguracionDAO;

public class ConfiguracionBusiness {

    private IConfiguracionDAO configuracionDAO;

    public ConfiguracionBusiness() {
         configuracionDAO = new ConfiguracionDAO();
    }

    public Configuracion getConfiguracion(String user, PreferencesUtils preferencesUtils) {
            Configuracion config = null;
            try{
                config = configuracionDAO.getConfiguracion(user,preferencesUtils);
            }catch (Exception e){
                e.printStackTrace();
            }
            return config;
    }

    public boolean save(Configuracion config, String user, PreferencesUtils preferencesUtils) {
        if (!configuracionDAO.save(config, user,preferencesUtils))
            return false;
        else
            return true;
    }

}
