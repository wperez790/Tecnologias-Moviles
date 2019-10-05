package org.moviles.persistance;

import org.json.JSONObject;
import org.moviles.Constants;
import org.moviles.Context;
import org.moviles.Util;
import org.moviles.model.Usuario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements IUsuarioDAO {

    public boolean save(Usuario user) {
        String json = getJSON(user);
        File file = new File(Context.getDataDir(),
                user.getUsuario()+"/"+Constants.USER_DATA_FILE);

        return Util.writeFile(file,json);
    }

    public Usuario getUsuario(String username) {
        File userData = new File(Context.getDataDir(),
                username + "/" + Constants.USER_DATA_FILE);
        if(!userData.exists())
            return null;

        String json = Util.readFile(userData);

        return getFromJSON(json);
    }

    public List<Usuario> getListaUsuarios() {
        List<Usuario> out = new ArrayList<Usuario>();
        File file = new File(Context.getDataDir(), Constants.USER_LIST_FILE);
        String[] list = Util.readFile(file).split("\n");
        if(list[0].equals("")){
            return out;
        }

        for(int i = 0; i < list.length; i++){
            Usuario u = getUsuario(list[i]);
            out.add(u);
        }
        return out;
    }

    public boolean setListaUsuarios(List<Usuario> list){
        File file = new File(Context.getDataDir(), Constants.USER_LIST_FILE);
        String listTxt = "";
        for(int i = 0; i < list.size(); i++){
            listTxt += list.get(i).getUsuario() + "\n";
        }

        return Util.writeFile(file,listTxt);
    }

    public Usuario getCurrentUser(){
        File loged = new File(Context.getDataDir(), Constants.CURRENT_USER_FILE);
        if(loged.exists()) {
            return getUsuario(Util.readFile(loged));
        }else
            return null;
    }

    public boolean setCurrentUser(String username){
        if(username == null){
            return Util.deleteFileOnPath(Context.getDataDir(),Constants.CURRENT_USER_FILE);
        }

        File loged = new File(Context.getDataDir(), Constants.CURRENT_USER_FILE);
        return Util.writeFile(loged,username);
    }

    public Usuario getFromJSON(String jsonUser){
        Usuario user;
        try {
            JSONObject json = new JSONObject(jsonUser);
            user = new Usuario();
            user.setUsuario(json.getString("usuario"));
            user.setPassword(json.getString("password"));
            user.setEmail(json.getString("email"));
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getJSON(Usuario user){
        try{
            JSONObject json = new JSONObject();
            json.put("usuario",user.getUsuario());
            json.put("password",user.getPassword());
            json.put("email",user.getEmail());
            return json.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }
}
