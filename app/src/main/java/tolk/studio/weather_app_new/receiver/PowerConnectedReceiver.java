package tolk.studio.weather_app_new.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import tolk.studio.weather_app_new.R;

public class PowerConnectedReceiver extends BroadcastReceiver {

    private int messageId = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("BroadcastReceiver")
                .setContentText("Не забудьте посмотреть погоду на завтра");
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(messageId++,builder.build());


    }
}
