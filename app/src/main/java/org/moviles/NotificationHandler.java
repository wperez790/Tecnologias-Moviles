package org.moviles;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.core.app.NotificationCompat;
import org.moviles.activity.R;

public class NotificationHandler {



    private Context context;

    public NotificationHandler(Context context) {
        this.context = context;
    }

    public void sendClimaNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.notification_message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Crear el Notification Channel para versiones de android posteriores a API 26.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            String description = context.getString(R.string.channel_description);
            NotificationChannel notificationChannel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(description);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(1, builder.build());

    }

    }
