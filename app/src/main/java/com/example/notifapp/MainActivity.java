package com.example.notifapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    private final static String CHANNEL_ID = "primary -channel";
    private int NOTIFICATION_ID = 0;
    private final static String ACTION_UPDATE_NOTIF = "astion-update-notif";
    private NotificationReceiver mReceiver = new NotificationReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "app notif",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        findViewById(R.id.buttonNotif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotification();
            }
        });
        findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationManager.cancel(NOTIFICATION_ID);
            }
        });

        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIF));
    }

    private void updateNotification(){
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.wisnu);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage)
                .setBigContentTitle("Notification updated!!"));

        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
    private NotificationCompat.Builder getNotificationBuilder() {

        Intent notificationIntent = new Intent(this, Main2Activity.class);
        PendingIntent notificationPendingIntent = PendingIntent.
                getActivity(this,NOTIFICATION_ID,notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("New Notification")
                .setContentText("This is context text")
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentIntent(notificationPendingIntent);



        return notifyBuilder;
    }


    private void sendNotification(){
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIF);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID,updateIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.addAction(R.drawable.ic_android_black_24dp, "Update NOtification", updatePendingIntent);

        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public class NotificationReceiver extends BroadcastReceiver{
        public NotificationReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_UPDATE_NOTIF)){
                updateNotification();
            }
        }
    }
}









