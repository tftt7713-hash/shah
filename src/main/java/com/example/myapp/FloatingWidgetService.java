package com.example.myapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import java.lang.SuppressWarnings;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatingWidgetService extends Service {
    private WindowManager windowManager;
    private ImageView floatingView;
    private static final String CHANNEL_ID = "FloatingWidgetChannel";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Notification notification;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle("Overlay Active")
                    .setContentText("Floating widget is running in background.")
                    .setSmallIcon(android.R.drawable.ic_menu_agenda)
                    .build();
        } else {
            @SuppressWarnings("deprecation")
            Notification oldNotification = new Notification.Builder(this)
                    .setContentTitle("Overlay Active")
                    .setContentText("Floating widget is running.")
                    .setSmallIcon(android.R.drawable.ic_menu_agenda)
                    .getNotification();
            notification = oldNotification;
        }

        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        floatingView = new ImageView(this);

        // 🎯 LINE NUMBER 58: AAPKA CODE YAHAN HAI
        // Yahan sirf 'boy' likhna hai, '.jpg' BILKUL NAHIN likhna hai.
        floatingView.setImageResource(R.drawable.boy);

        floatingView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        int layoutType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            @SuppressWarnings("deprecation")
            int oldType = WindowManager.LayoutParams.TYPE_PHONE;
            layoutType = oldType;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = 150;  
        params.height = 150; 
        params.type = layoutType;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 200;
        params.y = 200;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.addView(floatingView, params);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Floating Widget Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null && floatingView != null) {
            windowManager.removeView(floatingView);
        }
    }
}
