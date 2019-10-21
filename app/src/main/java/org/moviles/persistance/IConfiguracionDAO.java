package org.moviles.persistance;

import android.content.Context;

import org.moviles.PreferencesUtils;
import org.moviles.model.Configuracion;

public interface IConfiguracionDAO {

    public Configuracion getConfiguracion(String user, PreferencesUtils preferencesUtils);
    public boolean save(Configuracion config, String user, PreferencesUtils preferencesUtils);
}
