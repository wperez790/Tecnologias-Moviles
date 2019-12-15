package org.moviles.activity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.moviles.Constants;
import org.moviles.Contexto;
import org.moviles.PreferencesUtils;
import org.moviles.Receiver;
import org.moviles.Util;
import org.moviles.business.ClimaBusiness;
import org.moviles.business.ConfiguracionBusiness;
import org.moviles.model.Clima;
import org.moviles.model.Configuracion;
import org.moviles.model.Usuario;

import java.util.Calendar;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {

    public static final int REQUEST_CODE=101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Contexto.setContext(getApplicationContext());
        Contexto.setDataDir(getApplicationContext().getDataDir());

        Usuario aux = Contexto.getUsuarioBusiness().getCurrentUser();
        if(aux != null){
            manageAlarms();
            Contexto.getUsuarioBusiness().setMantenerSesion(true);
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
            finish();
            return;
        }else{
            setClimaByDefault();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }

    private void setClimaByDefault() {

        ClimaBusiness cBO= Contexto.getClimaBusiness(getApplication());
        Clima climaDefault = new Clima();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        climaDefault.setAnio(c.get(Calendar.YEAR));
        climaDefault.setDiaNumero(c.get(Calendar.DAY_OF_MONTH));
        climaDefault.setMes(Util.getMesString(c.get(Calendar.MONTH)));
        climaDefault.setHumedad(40.0);
        climaDefault.setVientoVelocidad(10.0);
        climaDefault.setTemperatura(26.0);
        climaDefault.setTempMax(27.5);
        climaDefault.setTempMin(25.4);
        climaDefault.setCiudad("Córdoba");
        climaDefault.setDescripcion("este es el clima by default");
        cBO.insert(climaDefault);
    }

    private void sendWelcomeNotification() {

        //Setear una acción a la notificación:
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        //Crear el Notification Channel para versiones de android posteriores a API 26.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.channel_description);
            NotificationChannel notificationChannel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(description);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(1, builder.build());
    }

    private void manageAlarms(){
        /*Notifications*/
        //sendWelcomeNotification();
        AlarmManager alarmManager= (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);

        Intent intent=new Intent(this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        String user = Contexto.getUsuarioBusiness().getCurrentUser().getUsuario();
        ConfiguracionBusiness configBO = Contexto.getConfiguracionBusiness();
        PreferencesUtils preferencesUtils = new PreferencesUtils(getApplicationContext());
        Configuracion config =  configBO.getConfiguracion(user, preferencesUtils);

        if(config.isNotificaciones()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String[] auxString = config.getHora().split(":");

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(auxString[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(auxString[1]));


            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        else{
            /*
            With FLAG_NO_CREATE it will return null if the PendingIntent doesnt already exist. If it already exists it returns
            reference to the existing PendingIntent
            */
            PendingIntent pendingIntentCancelled = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_NO_CREATE);
            if (pendingIntent != null)
                alarmManager.cancel(pendingIntentCancelled);
        }
        /**/
    }
}

