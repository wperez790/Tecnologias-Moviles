package org.moviles.business;

import org.moviles.model.Configuracion;
import org.moviles.persistance.ConfiguracionDAO;
import org.moviles.persistance.IConfiguracionDAO;

public class ConfiguracionBusiness {

    private IConfiguracionDAO configuracionDAO;

    public ConfiguracionBusiness() {
         configuracionDAO = new ConfiguracionDAO();
    }

    public Configuracion getConfiguracion(String user) {
            Configuracion config = null;
            try{
                config = configuracionDAO.getConfiguracion(user);
            }catch (Exception e){
                e.printStackTrace();
            }
            return config;
    }

    public boolean save(Configuracion config, String user) {
        if (!configuracionDAO.save(config, user))
            return false;
        else
            return true;
    }

}
