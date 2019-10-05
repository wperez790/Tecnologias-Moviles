package org.moviles.persistance;

import org.moviles.model.Usuario;

import java.util.List;

public interface IUsuarioDAO {
    public Usuario getUsuario(String username);
    public boolean save(Usuario u);
    public List<Usuario> getListaUsuarios();
    public boolean setListaUsuarios(List<Usuario> list);
    public Usuario getCurrentUser();
    public boolean setCurrentUser(String username);
}
