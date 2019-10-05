package org.moviles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.moviles.Context;
import org.moviles.Util;
import org.moviles.business.UsuarioBusiness;
import org.moviles.model.Usuario;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class RegistrarUsuarioActivity extends AppCompatActivity {

    private EditText nuevoUsuario;
    private EditText nuevaPassword;
    private EditText nuevoCorreo;
    private Button btnAgregar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);
        setTitle("Crear usuario");

        nuevoUsuario = findViewById(R.id.nuevoUsuario);
        nuevaPassword = findViewById(R.id.nuevaPassword);
        nuevoCorreo = findViewById(R.id.nuevoCorreo);
        btnAgregar = findViewById(R.id.btnAgregarUsuario);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario(v);
            }
        });
    }

    private void registrarUsuario(View v){

        Usuario u = new Usuario();
        u.setUsuario(nuevoUsuario.getText().toString());
        u.setPassword(nuevaPassword.getText().toString());
        u.setEmail(nuevoCorreo.getText().toString());
        boolean valid = Util.checkUser(getApplicationContext(),u);

        if(!valid)
            return;

        UsuarioBusiness userBO = Context.getUsuarioBusiness();
        if(userBO.getUsuario(nuevoUsuario.getText().toString()) != null){
            Toast.makeText(v.getContext(),"El usuario ya existe", Toast.LENGTH_SHORT).show();
            return;
        }

        valid = userBO.save(u);

        if(valid)
            Toast.makeText(v.getContext(),"Se creo correctamente", Toast.LENGTH_SHORT).show();

        else
            Toast.makeText(v.getContext(),"No se creo correctamente", Toast.LENGTH_SHORT).show();


        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
