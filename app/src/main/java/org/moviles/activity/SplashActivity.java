package org.moviles.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.moviles.Context;
import org.moviles.model.Usuario;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Context.setDataDir(getApplicationContext().getDataDir());

        Usuario aux = Context.getUsuarioBusiness().getCurrentUser();
        if(aux != null){
            Context.getUsuarioBusiness().setMantenerSesion(true);
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            finish();
            return;
        }else{
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
