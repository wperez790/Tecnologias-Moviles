package org.moviles.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class LoginActivityBackup extends AppCompatActivity {

    private EditText userInput;
    private EditText passInput;
    private Button btnIngresar;
    private Button btnCrearUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        openMenu();

        super.onCreate(savedInstanceState);

        /*setContentView(R.layout.activity_main);

        userInput = findViewById(R.id.userInput);
        passInput = findViewById(R.id.passInput);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuario);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingresar();
            }
        });

        btnCrearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });*/
    }

    private void ingresar(){
        File file = new File(getApplicationContext().getFilesDir().toString()+"/"+userInput.getText().toString()+"/password.txt");
        String user = "";

        if(!file.exists()){
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.errorLogin),
                    Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                user += line;
            }
            br.close();

            JSONObject json = new JSONObject(user);
            if(json.get("contrasena").equals(passInput.getText().toString())){

                File fileLogin = new File(getApplicationContext().getDataDir(),"loggedSession.txt");
                FileWriter fl = new FileWriter(fileLogin);
                fl.write(json.get("usuario").toString());
                fl.close();

                openMenu();
            }else{
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.errorLogin),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crearUsuario(){
        Intent i = new Intent(LoginActivityBackup.this, RegistrarUsuarioActivity.class);
        startActivity(i);
    }

    public void openMenu(){
        File logged = new File(getApplicationContext().getDataDir(),"loggedSession.txt");
        if(logged.exists()){
            Intent i = new Intent(this,MenuActivity.class);
            startActivity(i);
            finish();
        }
    }
}
