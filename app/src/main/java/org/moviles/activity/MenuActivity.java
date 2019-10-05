package org.moviles.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.moviles.Constants;
import org.moviles.Context;
import org.moviles.Util;
import org.moviles.activity.Fragments.FragmentClimaExtendido;
import org.moviles.activity.Fragments.FragmentConfiguracion;
import org.moviles.activity.Fragments.FragmentEditarUsuario;
import org.moviles.activity.Fragments.FragmentHome;
import org.moviles.activity.Fragments.FragmentListaUsuarios;
import org.moviles.activity.Fragments.FragmentMap;
import org.moviles.activity.Interfaces.IFragmentEditarUsuarioListener;
import org.moviles.model.Usuario;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IFragmentEditarUsuarioListener {


    private DrawerLayout drawer;
    private FrameLayout fragmentContainer;
    private TextView nombreUsuarioMenu;
    private TextView emailUsuarioMenu;
    private CircleImageView avatar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setTitle("Menu");

        fragmentContainer = findViewById(R.id.fragment_container);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nombreUsuarioMenu = headerView.findViewById(R.id.nombreUsuarioMenu);
        emailUsuarioMenu = headerView.findViewById(R.id.emailUsuarioMenu);
        avatar = headerView.findViewById(R.id.avatar);

        cargarUsuario();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        FragmentHome  fh = new FragmentHome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,fh);
        ft.commit();

        toggle.syncState();

    }



    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment f = fragmentManager.findFragmentById(R.id.fragment_container);
            if(!(f instanceof FragmentHome)) {
                cargarHome();
                return;
            }

            if(!Context.getUsuarioBusiness().isMantenerSesion())
                Context.getUsuarioBusiness().setCurrentUser(null);
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if(!Context.getUsuarioBusiness().isMantenerSesion())
            Context.getUsuarioBusiness().setCurrentUser(null);
        super.onDestroy();
    }

    private void cargarUsuario(){
        Usuario user = Context.getUsuarioBusiness().getCurrentUser();
        nombreUsuarioMenu.setText(user.getUsuario());
        emailUsuarioMenu.setText(user.getEmail());

        File img = new File(Context.getDataDir(),user.getUsuario()+"/"+ Constants.USER_AVATAR);
        Bitmap bmp = Util.getImage(img);
        avatar.setImageBitmap(bmp);
    }

    private void cerrarSesion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cerrarSesionMensaje)
                .setPositiveButton(R.string.ACEPTAR, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!Context.getUsuarioBusiness().setCurrentUser(null))
                            return;
                        Intent i = new Intent(MenuActivity.this,LoginActivity.class);
                        startActivity(i);
                        finish();
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

    private void cargarHome(){
        FragmentHome  fhome = new FragmentHome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,fhome);
        ft.commit();
    }

    private void cargarEditar(){
        FragmentEditarUsuario  fEdit = new FragmentEditarUsuario(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,fEdit);
        ft.commit();


    }

    private void cargarDetalle(){
        FragmentClimaExtendido fce = new FragmentClimaExtendido();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,fce);
        ft.commit();
    }

    private void cargarMapa(){
        FragmentMap fmap = new FragmentMap();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,fmap);
        ft.commit();
    }

    private void cargarConfig(){
        FragmentConfiguracion fconf = new FragmentConfiguracion();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container,fconf);
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_logout:
                cerrarSesion();
                break;
            case R.id.nav_map:
                cargarMapa();
                break;
            case R.id.nav_home:
                cargarHome();
                break;
            case R.id.nav_extend:
                cargarDetalle();
                break;
            case R.id.nav_config:
                cargarConfig();
                break;
            case R.id.nav_edit_profile:
                cargarEditar();


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void cerrarFramgemntEditarUsuario() {
        cargarHome();
    }
}
