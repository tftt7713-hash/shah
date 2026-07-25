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
import android.widget.ImageView; // 👈 Naya Import image lagane ke liye

public class FloatingWidgetService extends Service {
    private WindowManager windowManager;
    private ImageView floatingView; // 👈 View ko ImageView mein badal diya hai
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
        
        // 1. ImageView object banana
        floatingView = new ImageView(this);

        // ⭐⭐⭐ IMAGE KA NAAM YAHAN BADALNA HAI ⭐⭐⭐
        // 'friend_pic' aapki photo ka naam hai jo drawable folder mein hai.
        // Agar photo ka naam badalna hai, toh bas 'friend_pic' ki jagah wo naam likhein (bina .png lagaye)
        floatingView.setImageResource(R.drawable.friend_pic);

        // Photo ko pure 150x150 ke dabbe mein sahi se fit/crop karne ke liye
        floatingView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        // 2. Layout Type define karna naye aur purane android versions ke liye
        int layoutType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            @SuppressWarnings("deprecation")
            int oldType = WindowManager.LayoutParams.TYPE_PHONE;
            layoutType = oldType;
        }

        // 3. Layout Parameters set karne ka tareeka
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = 150;  // Width in pixels
        params.height = 150; // Height in pixels
        params.type = layoutType;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 200;
        params.y = 200;

        // 4. Window Manager initialize karke view add karna
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
