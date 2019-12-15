package org.moviles.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mapbox.mapboxsdk.Mapbox;

import org.moviles.Constants;
import org.moviles.Contexto;
import org.moviles.PreferencesUtils;
import org.moviles.Util;
import org.moviles.activity.Fragments.FragmentClimaExtendido;
import org.moviles.activity.Fragments.FragmentConfiguracion;
import org.moviles.activity.Fragments.FragmentEditarUsuario;
import org.moviles.activity.Fragments.FragmentHome;
import org.moviles.activity.Fragments.FragmentMap;
import org.moviles.activity.Interfaces.IFragmentConfiguracionListener;
import org.moviles.activity.Interfaces.IFragmentEditarUsuarioListener;
import org.moviles.business.ClimaBusiness;
import org.moviles.business.ConfiguracionBusiness;
import org.moviles.dto.ClimaDTO;
import org.moviles.dto.ClimaListDTO;
import org.moviles.model.Clima;
import org.moviles.model.Configuracion;
import org.moviles.model.Usuario;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IFragmentEditarUsuarioListener, IFragmentConfiguracionListener {


    private DrawerLayout drawer;
    private FrameLayout fragmentContainer;
    private FragmentTransaction fragmentTransaction;
    private TextView nombreUsuarioMenu;
    private TextView emailUsuarioMenu;
    private CircleImageView avatar;
    private static Gson gson;
    private static Clima climaActual;
    private static List<Clima> climaListActual;
    private ClimaBusiness cBO;
    private Location loc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Contexto.setContext(getApplicationContext());
        setTitle("Menu");

        cBO = Contexto.getClimaBusiness(getApplication());

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

        getFromApi("Córdoba");

        FragmentHome fh = new FragmentHome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fh);
        ft.commit();

        toggle.syncState();
    }


    public void getFromApi(String city) {
        if (isNetworkConnected()) {
            try {
                climaActual = new DownloadInfoClimaTask().execute(city).get();
                Contexto.clima= climaActual;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                loadLastFromDataBase();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void getLastDaysFromApi(){
        if (isNetworkConnected()) {
            try {
                climaListActual = new DownloadInfoListClimaTask().execute("Córdoba").get();
                Contexto.climaList = climaListActual;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                loadLastFromDataBase();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment f = fragmentManager.findFragmentById(R.id.fragment_container);
            if (!(f instanceof FragmentHome)) {
                cargarHome();
                return;
            }

            if (!Contexto.getUsuarioBusiness().isMantenerSesion())
                Contexto.getUsuarioBusiness().setCurrentUser(null);
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (!Contexto.getUsuarioBusiness().isMantenerSesion())
            Contexto.getUsuarioBusiness().setCurrentUser(null);
        super.onDestroy();
    }

    private void cargarUsuario() {
        Usuario user = Contexto.getUsuarioBusiness().getCurrentUser();
        nombreUsuarioMenu.setText(user.getUsuario());
        emailUsuarioMenu.setText(user.getEmail());
        File img = new File(Contexto.getDataDir(), user.getUsuario() + "/" + Constants.USER_AVATAR);
        Bitmap bmp = Util.getImage(img);
        avatar.setImageBitmap(bmp);
    }

    private void cerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cerrarSesionMensaje)
                .setPositiveButton(R.string.ACEPTAR, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!Contexto.getUsuarioBusiness().setCurrentUser(null))
                            return;
                        Intent i = new Intent(MenuActivity.this, LoginActivity.class);
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

    private void cargarHome() {
        FragmentHome fhome = new FragmentHome();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        /*Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("fragment_home", list);
        fhome.setArguments(bundle);*/
        ft.replace(R.id.fragment_container, fhome);
        ft.commit();
    }

    private void cargarEditar() {
        FragmentEditarUsuario fEdit = new FragmentEditarUsuario(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fEdit);
        ft.commit();


    }

    private void cargarDetalle() {
        getLastDaysFromApi();
        FragmentClimaExtendido fce = new FragmentClimaExtendido();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fce);
        ft.commit();
    }

    private void cargarMapa() {
        Mapbox.getInstance(this,Constants.MAPBOX_TOKEN);
        FragmentMap fmap = new FragmentMap();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fmap);

        ft.commit();
    }

    private void cargarConfig() {
        FragmentConfiguracion fconf = new FragmentConfiguracion(this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fconf);
        ft.commit();
    }

    private void cargarEnviarEmail() {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.EMAIL_DEVELOPER});
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.asuntoPorDefecto));
        try {
            startActivity(Intent.createChooser(i, getResources().getString(R.string.enviarEmail)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MenuActivity.this, getResources().getString(R.string.noClientesEmail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
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
                break;
            case R.id.nav_email:
                cargarEnviarEmail();


        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void cerrarFramgmentEditarUsuario() {
        cargarHome();
    }

    @Override
    public void actualizarUsuario() {
        cargarUsuario();
    }

    @Override
    public void cerrarFramgemntConfiguracion() {
        cargarHome();
    }

    @Override
    public void actualizarConfiguracion(Configuracion config) {
        ConfiguracionBusiness configBO = Contexto.getConfiguracionBusiness();
        String user = Contexto.getUsuarioBusiness().getCurrentUser().getUsuario();
        PreferencesUtils preferencesUtils = new PreferencesUtils(getApplicationContext());
        boolean valid = configBO.save(config, user, preferencesUtils);
        getFromApi(Contexto.getClima().getCiudad());
        cargarHome();
        if (valid)
            Toast.makeText(getApplicationContext(), getString(R.string.configuracionGuardada), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), getString(R.string.configuracionNoGuardada), Toast.LENGTH_SHORT).show();


    }


    // Método que chequea si hay conexión a Internet.
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean connected = false;
        if (networkInfo != null && networkInfo.isConnected())
            connected = true;
        else {
            showDialogNoInternet();

        }

        return connected;
    }

    private void loadLastFromDataBase() {
        Location location = getLastLocation();
        Clima c =null;
        if(location!=null)
            c = cBO.getClimaByLocation(location);
        if (c == null)
            c = cBO.getClimaByCity("Córdoba");
        if (c != null)
            Contexto.setClima(c);


    }

    public Location getLastLocation() {
        requestPermissionsLocation();
        if(ActivityCompat.checkSelfPermission(MenuActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return null;
        }
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            loc= location;
                        }
                    }
                });
        return loc;
    }



    public void requestPermissionsLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PackageManager.GET_PERMISSIONS);
        }
    }

    private void showDialogNoInternet(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.no_internet)
                .setPositiveButton(R.string.reintentar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isNetworkConnected();
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setTitle("Atención!")
                .create();
        builder.show();
    }
    /*private Clima getClimaByLocation(){

    }*/
    private  Clima  getClimaByCity(String city) throws IOException {
        InputStream source = retrieveStreamByCity(city);

        String res =  convertStreamToString(source);

        Gson gson = new Gson();
        ClimaDTO climaDTO = gson.fromJson(res,ClimaDTO.class);
        Clima clima = climaDTO.getClima();
        Contexto.setClima(clima);
        ClimaBusiness cBO =  Contexto.getClimaBusiness(getApplication());
        cBO.insert(clima);
            return clima;

    }
    private List<Clima> getClimaListByCity(String city) throws IOException {

        InputStream source = retrieveStreamListByCity(city);

        String res =  convertStreamToString(source);

        Gson gson = new Gson();
        ClimaListDTO climaListDTO = gson.fromJson(res,ClimaListDTO.class);
        List<Clima> climaList = climaListDTO.getListClima();
        Contexto.setClimaList(climaList);
        ClimaBusiness cBO =  Contexto.getClimaBusiness(getApplication());
        cBO.insertClimas(climaList);
        return climaList;
    }

    public static String convertStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public InputStream retrieveStreamByCity(String city) throws IOException {
        URL url = null;
        ConfiguracionBusiness configBO = Contexto.getConfiguracionBusiness();
        String user = Contexto.getUsuarioBusiness().getCurrentUser().getUsuario();
        PreferencesUtils preferencesUtils = new PreferencesUtils(getApplicationContext());
        Configuracion config = configBO.getConfiguracion(user, preferencesUtils);
        String unidad=  config.getUnidad().split(" ")[0];
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city +"&units="+unidad+"&lang=es&appid="+Constants.API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return new BufferedInputStream(connection.getInputStream());
    }

    private InputStream retrieveStreamListByCity(String city) throws IOException {
        URL url = null;
        ConfiguracionBusiness configBO = Contexto.getConfiguracionBusiness();
        String user = Contexto.getUsuarioBusiness().getCurrentUser().getUsuario();
        PreferencesUtils preferencesUtils = new PreferencesUtils(getApplicationContext());
        Configuracion config = configBO.getConfiguracion(user, preferencesUtils);
        String unidad=  config.getUnidad().split(" ")[0];
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/forecast?q=" + city +"&units="+unidad+"&cnt="+Constants.CANTIDAD_DEFAULT+"&lang=es&appid="+Constants.API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return new BufferedInputStream(connection.getInputStream());
    }


    private class DownloadInfoClimaTask extends AsyncTask<String, Void, Clima>{

        @Override
        protected Clima doInBackground(String... cities) {
            try {
                return getClimaByCity(cities[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Clima clima) {
            super.onPostExecute(clima);
            cargarHome();
        }
    }

    private class DownloadInfoListClimaTask extends AsyncTask<String, Void, List<Clima>>{

        @Override
        protected List<Clima> doInBackground(String... cities) {
            try {
                return getClimaListByCity(cities[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Clima> climaList) {
            super.onPostExecute(climaList);
            cargarDetalle();
        }
    }




}
