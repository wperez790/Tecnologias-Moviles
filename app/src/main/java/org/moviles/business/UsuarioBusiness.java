package org.moviles.business;

import org.moviles.model.Usuario;
import org.moviles.persistance.IUsuarioDAO;
import org.moviles.persistance.UsuarioDAO;

import java.util.ArrayList;
import java.util.List;

public class UsuarioBusiness {
    private IUsuarioDAO usuarioDAO;
    private Usuario currentUser;
    private List<Usuario> listaUsuarios;
    private boolean mantenerSesion;

    public UsuarioBusiness() {
        usuarioDAO = new UsuarioDAO();
        getCurrentUser();
    }

    public boolean save(Usuario u) {
        if(!usuarioDAO.save(u))
            return false;

        getListaUsuarios();
        listaUsuarios.add(u);
        setListaUsuarios(listaUsuarios);

        return true;
    }

    public boolean update(Usuario u){
        return usuarioDAO.save(u);
    }

    public Usuario getUsuario(String username) {
        Usuario u = null;
        try {
            u = usuarioDAO.getUsuario(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return u;
    }

    public List<Usuario> getListaUsuarios() {
        if(listaUsuarios == null)
            listaUsuarios = usuarioDAO.getListaUsuarios();

        return new ArrayList<Usuario>(listaUsuarios);
    }

    public boolean setListaUsuarios(List<Usuario> list){
        if(usuarioDAO.setListaUsuarios(list)){
            listaUsuarios = list;
            return true;
        }
        return false;
    }

    public Usuario getCurrentUser(){
        if(currentUser == null) {
            currentUser = usuarioDAO.getCurrentUser();
        }

        return currentUser;
    }

    public boolean setCurrentUser(Usuario user){
        String username;
        if(user == null)
            username = null;
        else
            username = user.getUsuario();

        if(usuarioDAO.setCurrentUser(username)){
            currentUser = user;
            return true;
        }
        return false;
    }

    public void changeUserNameList(String oldName,String newName){
        boolean found = false;
        for(Usuario u :listaUsuarios){
            if(u.getUsuario().equals(oldName)) {
                u.setUsuario(newName);
                found = true;
            }
        }
        if(found){
            setListaUsuarios(listaUsuarios);
        }
    }

    public boolean isMantenerSesion() {
        return mantenerSesion;
    }

    public void setMantenerSesion(boolean mantenerSesion) {
        this.mantenerSesion = mantenerSesion;
    }
}
