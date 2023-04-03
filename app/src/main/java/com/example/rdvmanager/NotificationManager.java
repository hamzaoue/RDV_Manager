package com.example.rdvmanager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/********************/
public class NotificationManager extends BroadcastReceiver
{
    public static String CHANNEL_NAME = "appointment_channel";
    /********************/
    @Override
    public void onReceive(Context context, Intent intent)
    {
        long id = intent.getExtras().getLong("id", -1);
        this.setNotificationChannel(context);
        this.sendNotification(context, this.createNotification(context, id),id);
        //
        long time = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String date = sdf.format(new Date(time));
        Log.d("Alarme déclanchée à ",date);
        //
    }
    /********************/
    private void sendNotification(Context context, Notification notification, long id)
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(!prefs.getBoolean("Notifications",false))
            return;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED)
            return;
        notificationManager.notify((int) id, notification);
    }
    /********************/
    private void setNotificationChannel(Context context)
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            NotificationChannel channel = new NotificationChannel(CHANNEL_NAME, CHANNEL_NAME,
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channel.setVibrationPattern(new long[] { 1000, 1000, 1000, 1000, 1000 });
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }
    /********************/
    private Notification createNotification(Context context, long id)
    {
        AppointmentDataBase appointmentDataBase = new AppointmentDataBase(context);
        Appointment appointment = appointmentDataBase.getAppointmentById(id);
        appointmentDataBase.close();
        String message = getMessage(context, appointment);
        PendingIntent intent = getIntent(context);
        return buildNotification(context, message, intent);
    }
    /********************/
    private String getMessage(Context context, Appointment appointment)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int[] optionsId = { R.string.two_hours, R.string.one_day, R.string.one_week };
        String time = context.getString(optionsId[prefs.getInt("NotificationIndex", 0)]);
        return context.getString(R.string.rappel_appointment_in_time, appointment.getTitle(), time);
    }
    /********************/
    private PendingIntent getIntent(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
    /********************/
    private Notification buildNotification(Context context, String message, PendingIntent intent)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_NAME);
        builder.setSmallIcon(R.drawable.ic_event);
        builder.setContentText(message);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentIntent(intent);
        builder.setAutoCancel(true);
        return builder.build();
    }
    /********************/
    public static void updateAllAlarms(Context context)
    {
        AppointmentDataBase appointmentDataBase = new AppointmentDataBase(context);
        List<Appointment> appointmentList = appointmentDataBase.getAppointmentList("");
        for(Appointment appointment: appointmentList)
            NotificationManager.setAlarm(context,appointment);
        appointmentDataBase.close();
    }
    /********************/
    public static void setAlarm(Context context, Appointment appointment)
    {
        long time = appointment.getCalendar().getTimeInMillis() - getPreferenceTime(context);
        if (time < System.currentTimeMillis()) return;
        long id = appointment.getId();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationManager.class);
        intent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        //
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String date = sdf.format(new Date(time));
        Log.d("Alarme programmé pour ",date);
        //
    }
    /********************/
    public static void cancelAlarm(Context context, Appointment appointment)
    {
        long id = appointment.getId();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationManager.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) id, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
    /********************/
     public static  int getPreferenceTime(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        switch(prefs.getInt("NotificationIndex",0)) {
            case 0:return 2*3600*1000;
            case 1:return 24*3600*1000;
            case 2:return 24*3600*7*1000;
            default:return 0;
        }
    }
}
