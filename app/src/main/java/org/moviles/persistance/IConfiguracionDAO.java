package org.moviles.persistance;

import org.moviles.model.Configuracion;

public interface IConfiguracionDAO {

    public Configuracion getConfiguracion(String user);
    public boolean save(Configuracion config, String user);
}
