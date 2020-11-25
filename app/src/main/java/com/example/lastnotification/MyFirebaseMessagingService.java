package com.example.lastnotification;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.example.lastnotification.MainActivity;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Notification.DEFAULT_ALL;
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        System.out.println("From: " + remoteMessage.getFrom());
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            System.out.println("Notification Body: " + remoteMessage.getNotification().getBody());
//            //handleNotification(remoteMessage.getNotification().getBody());
//            showNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
//
//        }
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                System.out.println(remoteMessage.getData().get("title"));
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
                //showNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(JSONObject json) throws Exception {
        System.out.println("Json:"+json);
        showNotification(json.getString("title"),json.getString("body"));
    }

    private void showNotification(String title,String message){
         /*JSONObject data = jsonObject.getJSONObject("data");

        String title=data.getString("title");
        String message = data.getString("message");*/
        System.out.println("Message:"+message);
        Intent notificationIntent=new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getBaseContext(), Config.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentTitle(title)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationManagerCompat.IMPORTANCE_MAX)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setColorized(true)
                .setColor(Color.RED)
                .setDefaults(DEFAULT_ALL);
        NotificationManager notificationManager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID,builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        System.out.println("New Token:"+s);
        super.onNewToken(s);
    }
}
