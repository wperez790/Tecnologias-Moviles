package org.moviles.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;
import org.moviles.Context;
import org.moviles.Util;
import org.moviles.activity.Fragments.FragmentIngresarContraseña;
import org.moviles.activity.Fragments.FragmentListaUsuarios;
import org.moviles.activity.Interfaces.ListaUsuarioRecyclerViewOnItemClickListener;
import org.moviles.business.UsuarioBusiness;
import org.moviles.model.Usuario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements ListaUsuarioRecyclerViewOnItemClickListener {

    private List<Usuario> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        cargarFragmentoLista(false);
    }

    public void cargarFragmentoLista(boolean useAnimation){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        cargarLista();

        FragmentListaUsuarios fragmentListaUsuarios = new FragmentListaUsuarios(this, usersList);

        if(useAnimation)
            fragmentTransaction.setCustomAnimations(
                    R.anim.entrar_por_izquierda,
                    R.anim.salir_por_derecha,
                    R.anim.entrar_por_derecha,
                    R.anim.salir_por_izquierda);

        fragmentTransaction.replace(R.id.FragmentListaUsuarios, fragmentListaUsuarios);

        fragmentTransaction.commit();
    }

    public void cargarFragmentoContraseña(int position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        try {
            FragmentIngresarContraseña fragmentIngresarContraseña = new FragmentIngresarContraseña(this,
                    usersList.get(position).getUsuario());

            fragmentTransaction.setCustomAnimations(R.anim.entrar_por_derecha,R.anim.salir_por_izquierda,R.anim.entrar_por_izquierda,R.anim.salir_por_derecha);
            fragmentTransaction.replace(R.id.FragmentListaUsuarios, fragmentIngresarContraseña);

            fragmentTransaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void cargarLista(){
        usersList = Context.getUsuarioBusiness().getListaUsuarios();

        Usuario aux = new Usuario();
        try {
            aux.setUsuario(getString(R.string.crearUSuario));
            aux.setEmail("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        usersList.add(0,aux);
    }


    @Override
    public void onClickItem(int position) {
        Intent i;
        if(position == 0) {
            i = new Intent(this, RegistrarUsuarioActivity.class);
            startActivity(i);
            finish();

        }else {
            cargarFragmentoContraseña(position);
        }
    }

    @Override
    public void onClickDelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.eliminarUsuarioMensaje)
                .setPositiveButton(R.string.ACEPTAR, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        borrarUsuario(position);
                    }
                })
                .setNegativeButton(R.string.CANCELAR, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    public void borrarUsuario(int position){

        boolean valid = Util.deleteFileOnPath(Context.getDataDir(),
                usersList.get(position).getUsuario());

        if(!valid){
            Toast.makeText(getApplicationContext(),getString(R.string.errorEliminarUsuario),Toast.LENGTH_SHORT).show();
            return;
        }

        usersList.remove(position);
        usersList.remove(0);
        Context.getUsuarioBusiness().setListaUsuarios(usersList);
        cargarLista();

        cargarFragmentoLista(false);
        Toast.makeText(getApplicationContext(),getString(R.string.usuarioEliminado),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickIngresar(String user, String password, boolean mantenerSesion){
        UsuarioBusiness userBO = Context.getUsuarioBusiness();
        Usuario u = userBO.getUsuario(user);
        if(!u.getPassword().equals(password)){
            Toast.makeText(getApplicationContext(),
                    getString(R.string.ingresoNoValido),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        userBO.setCurrentUser(u);
        userBO.setMantenerSesion(mantenerSesion);

        Intent i = new Intent(this,MenuActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment f = fragmentManager.findFragmentById(R.id.FragmentListaUsuarios);

        if(f instanceof FragmentListaUsuarios)
            super.onBackPressed();
        else
            cargarFragmentoLista(true);
    }
}
