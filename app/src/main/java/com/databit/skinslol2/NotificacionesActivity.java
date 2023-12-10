package com.databit.skinslol2;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.util.TimeZone;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import java.util.HashSet;
import java.util.Set;
import android.widget.CompoundButton;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificacionesActivity extends AppCompatActivity {
    Button btnNoti1;
    Button btnNoti2;
    Button btnNoti3;
    ImageView tuerquita;
    ImageView tuerquita2;
    Switch switchUno;
    Switch switchDos;
    String[] options;
    private RadioButton radioVibraciones;
    private RadioButton radioUltimoDia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        btnNoti1 = (Button) findViewById(R.id.btnMainNotificaciones);
        btnNoti1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btn1 = new Intent(NotificacionesActivity.this, NotificacionesActivity.class);
                startActivity(btn1);
            }
        });
        btnNoti2 = (Button) findViewById(R.id.btnOfertasNoti);
        btnNoti2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnNoti2 = new Intent(NotificacionesActivity.this, MainActivity.class);
                startActivity(btnNoti2);
            }
        });
        btnNoti3 = (Button) findViewById(R.id.btnParcheNoti);
        btnNoti3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnNoti3 = new Intent(NotificacionesActivity.this, ParcheActivity.class);
                startActivity(btnNoti3);
            }
        });
        tuerquita = findViewById(R.id.tuerquita);
        switchUno = findViewById(R.id.switchUno);
        tuerquita2 = findViewById(R.id.tuerquita2);
        switchDos = findViewById(R.id.switchDos);
        radioVibraciones = new RadioButton(this);
        radioUltimoDia = new RadioButton(this);
        boolean switchState1 = loadSwitchState();
        switchUno.setChecked(switchState1);
        updateOptionsAndImageEnabled1(switchState1);
        tuerquita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switchUno.isChecked()) {
                    showOptionsDialog1();
                }
            }
        });

        switchUno.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            saveSwitchState1(isChecked1);
            updateOptionsAndImageEnabled1(isChecked1);
        });
        boolean switchDosState = loadSwitchDosState();
        switchDos.setChecked(switchDosState);
        updateOptionsAndImageEnabled2(switchDosState);
        switchDos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                saveSwitchDosState(isChecked);
                saveSwitchState2(isChecked);
                updateNotificationEnabled(isChecked);
                updateOptionsAndImageEnabled2(isChecked);
                if (isChecked) {
                    Calendar calendarHoy = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
                    if (calendarHoy.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        showWednesdayNotification2();
                    }
                    showOptionsDialog2();
                }
                scheduleNotifications();
            }
        });
    }
    private void showOptionsDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona opciones");
        String[] options = {"Atropos", "Ahri", "Akali", "Akshan", "Alistar", "Amumu", "Anivia", "Annie", "Aphelios", "Ashe", "Azir", "Aurelion Sol", "Bardo", "Bel'Veth", "Blitzcrank", "Brand", "Briar", "Caitlyn", "Camille", "Cassiopeia", "Cho'Gath", "Corki", "Darius", "Diana", "Draven", "Dr.Mundo", "Ekko", "Elise", "Evelynn", "Ezreal", "Fiddlesticks", "Fiora", "Galio", "Gankplank", "Gnar", "Gragas", "Graves", "Gwen", "Hecarim", "Heimerdinger", "Illaoi", "Irelia", "Ivern", "Janna", "JarvanIV", "Jax", "Jayce", "Jhin", "Jinx", "Kai'sa", "Kalista", "Karma", "Karthus", "Kassadin", "Katarina", "Kayle", "Kayn", "Kennen", "Kled", "Kog'Maw", "Lillia", "Lissandra", "Lucian", "Lulu", "Lux", "Maestro Yi", "Malzahar", "Maokai", "Milio", "Miss Fortune", "Mordekaiser", "Morgana", "Naafiri", "Nami", "Nasus", "Nautilus", "Neeko", "Nidalee", "Nilah", "Nocturne", "Nunu y Willump", "Olaf", "Orianna", "Ornn", "Pantheon", "Poppy", "Pyke", "Qiyana", "Quinn", "Rakan", "Rammus", "Rek'Sai", "Rell", "Renata Glasc", "Renekton", "Riven", "Rumble", "Ryze", "Samira", "Sejuani", "Senna", "Seraphine", "Sett", "Shaco", "Shen", "Shyvana", "Singed", "Sion", "Sivir", "Sona", "Soraka", "Swain", "Sylas", "Syndra", "Tahm Kench", "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle", "Tryndamere", "Twisted Fate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar", "Vel'Koz", "Vex", "Vi", "Viego", "Viktor", "Vladimir", "Volibear", "Warwick", "Wukong", "Xayah", "Xerath", "Xin Zhao", "Yasuo", "Yuumi", "Zed", "Zeri", "Ziggs", "Zilean", "Zoe", "Zyra"};
        boolean[] selectedOptions = loadSelectedOptions();

        builder.setMultiChoiceItems(options, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                selectedOptions[i] = isChecked;
            }
        });
        builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                saveSelectedOptions(selectedOptions);
                for (int j = 0; j < selectedOptions.length; j++) {
                    if (selectedOptions[j]) {
                        Log.d("Option", options[j] + " seleccionada");
                    }
                }
                showWednesdayNotification();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }
    private void saveSwitchState1(boolean switchState1) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("switch_state", switchState1);
        editor.apply();
    }
    private boolean loadSwitchState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("switch_state", false);
    }
    private void saveSelectedOptions(boolean[] selectedOptions) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> selectedSet = new HashSet<>();
        for (int i = 0; i < selectedOptions.length; i++) {
            if (selectedOptions[i]) {
                selectedSet.add(String.valueOf(i));
            }
        }
        editor.putStringSet("selected_options", selectedSet);
        editor.apply();
    }
    private boolean[] loadSelectedOptions2() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selectedSet = preferences.getStringSet("selected_options2", new HashSet<>());
        boolean[] selectedOptions = new boolean[2];
        for (String index : selectedSet) {
            int i = Integer.parseInt(index);
            if (i >= 0 && i < selectedOptions.length) {
                selectedOptions[i] = true;
            }
        }
        return selectedOptions;
    }
    private boolean[] loadSelectedOptions() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selectedSet = preferences.getStringSet("selected_options", new HashSet<>());
        boolean[] selectedOptions = new boolean[156];
        for (String index : selectedSet) {
            int i = Integer.parseInt(index);
            if (i >= 0 && i < selectedOptions.length) {
                selectedOptions[i] = true;
            }
        }
        return selectedOptions;
    }
    private void updateOptionsAndImageEnabled1(boolean isEnabled) {
        if (isEnabled) {
            Log.d("Switch", "Switch habilitado");
        } else {
            Log.d("Switch", "Switch deshabilitado");
        }
        tuerquita.setEnabled(isEnabled);
    }
    private void showWednesdayNotification() {
        TimeZone timeZoneChile1 = TimeZone.getTimeZone("Chile/Continental");
        Calendar calendar = Calendar.getInstance(timeZoneChile1);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && switchUno.isChecked()) {
            boolean[] selectedOptions = loadSelectedOptions();
            boolean atLeastOneOptionSelected = false;
            for (boolean option : selectedOptions) {
                if (option) {
                    atLeastOneOptionSelected = true;
                    break;
                }
            }
            if (atLeastOneOptionSelected) {
                long tiempoRestanteHastaMiercoles = calculateTimeUntilNextWednesday();
                scheduleNotification(tiempoRestanteHastaMiercoles);
            }
        }
    }
    private void scheduleDailyVibrationNotifications() {

        TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
        Calendar calendarActual = Calendar.getInstance(timeZoneChile);
        Calendar calendarInicio = Calendar.getInstance(timeZoneChile);
        calendarInicio.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);

        if (calendarActual.after(calendarInicio)) {
            calendarInicio.add(Calendar.DATE, 7);
        }
        Calendar calendarFin = (Calendar) calendarInicio.clone();
        calendarFin.add(Calendar.DATE, 5);
        while (calendarInicio.before(calendarFin)) {
            scheduleNotification(calendarInicio.getTimeInMillis());
            calendarInicio.add(Calendar.DATE, 1);
        }
        disableSavedOptions();
    }
    private long calculateTimeUntilNextWednesday() {

        Calendar calendarHoy = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        Calendar calendarProximoMiercoles = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        calendarProximoMiercoles.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendarProximoMiercoles.set(Calendar.HOUR_OF_DAY, 0);
        calendarProximoMiercoles.set(Calendar.MINUTE, 0);
        calendarProximoMiercoles.set(Calendar.SECOND, 0);
        calendarProximoMiercoles.set(Calendar.MILLISECOND, 0);

        if (calendarHoy.after(calendarProximoMiercoles)) {
            calendarProximoMiercoles.add(Calendar.DATE, 7);
        }
        return calendarProximoMiercoles.getTimeInMillis() - calendarHoy.getTimeInMillis();
    }
    private void showOptionsDialog2() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecciona opciones para tuerquita2");
            String[] options = {"Notificar todos los días", "Notificar el último día"};
            int selectedOption = loadSelectedOption2();
            builder.setSingleChoiceItems(options, selectedOption, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        saveSelectedOption2(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        int selectedOption = loadSelectedOption2();
                        handleNotificationOption(selectedOption);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        dialogInterface.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void saveSelectedOption2(int selectedOption) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selected_option2", selectedOption);
        editor.apply();
    }
    private int loadSelectedOption2() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("selected_option2", 0);
    }
    private void scheduleNotification(long delayMillis) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionesActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent);
        }
    }
    private void handleNotificationOption(int selectedOption) {
        try {
            switch (selectedOption) {
                case 0:
                    scheduleDailyNotificationsForTuerquita2();
                    break;
                case 1:
                    scheduleTuesdayNotification();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scheduleDailyNotificationsForTuerquita2() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 7);
        }
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent
        );
    }
    public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id", "Nombre del canal", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setContentTitle("Título de la notificación")
                    .setContentText("Contenido de la notificación")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            notificationManager.notify(1, builder.build());
        }
    }
    private void updateOptionsAndImageEnabled2(boolean isEnabled) {
        if (isEnabled) {
            Log.d("Tuerquita2", "Opciones e imagen habilitadas");
            tuerquita2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (switchDos.isChecked()) {
                        showOptionsDialog2();
                    }
                }
            });
        } else {
            Log.d("Tuerquita2", "Opciones e imagen deshabilitadas");
            tuerquita2.setOnClickListener(null);
        }
    }
    private void scheduleTuesdayNotification() {
        try {
            TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
            Calendar calendarHoy = Calendar.getInstance(timeZoneChile);
            Calendar calendarProximoMartes = Calendar.getInstance(timeZoneChile);
            calendarProximoMartes.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            calendarProximoMartes.set(Calendar.HOUR_OF_DAY, 0);
            calendarProximoMartes.set(Calendar.MINUTE, 0);
            calendarProximoMartes.set(Calendar.SECOND, 0);
            calendarProximoMartes.set(Calendar.MILLISECOND, 0);

            if (calendarHoy.after(calendarProximoMartes)) {
                calendarProximoMartes.add(Calendar.DATE, 7);
            }
            long tiempoRestanteHastaMartes = calendarProximoMartes.getTimeInMillis() - calendarHoy.getTimeInMillis();
            scheduleNotification(tiempoRestanteHastaMartes, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void scheduleNotification(long delayMillis, boolean withVibration) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificacionesActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int notificationFlags = 0;
        if (withVibration) {
            notificationFlags |= Notification.DEFAULT_VIBRATE;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Título de la notificación")
                .setContentText("Texto de la notificación")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setDefaults(notificationFlags)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }
    private void scheduleNotifications() {
        try {
            if (switchDos.isChecked()) {
                int savedNotificationOption = loadNotificationOption();

                if (savedNotificationOption == radioVibraciones.getId()) {
                    scheduleDailyVibrationNotifications();
                } else if (savedNotificationOption == radioUltimoDia.getId()) {
                    scheduleTuesdayNotification();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void disableSavedOptions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notification_option", 0);
        editor.apply();
    }
    private int loadNotificationOption() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("notification_option", 0);
    }
    private void saveSwitchDosState(boolean switchDosState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("switch_dos_state", switchDosState);
        editor.apply();
    }
    private boolean loadSwitchDosState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("switch_dos_state", false);
    }
    private void updateNotificationEnabled(boolean isEnabled) {
        if (isEnabled) {
            showOptionsDialog2();
        } else {
            disableSavedOptions();
        }
    }
    private void saveSwitchState2(boolean switchState2) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("switch_state", switchState2);
        editor.apply();
    }
    private void showWednesdayNotification2() {
        TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
        Calendar calendar = Calendar.getInstance(timeZoneChile);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && switchDos.isChecked()) {
            boolean[] selectedOptions = loadSelectedOptions();

            boolean atLeastOneOptionSelected = false;
            for (boolean option : selectedOptions) {
                if (option) {
                    atLeastOneOptionSelected = true;
                    break;
                }
            }
            if (atLeastOneOptionSelected) {
                long tiempoRestanteHastaMiercoles2 = calculateTimeUntilNextWednesday2();
                scheduleNotification2(tiempoRestanteHastaMiercoles2);
            }
        }
    }
    private long calculateTimeUntilNextWednesday2() {

        Calendar calendarHoy = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        Calendar calendarProximoMiercoles = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        calendarProximoMiercoles.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendarProximoMiercoles.set(Calendar.HOUR_OF_DAY, 0);
        calendarProximoMiercoles.set(Calendar.MINUTE, 0);
        calendarProximoMiercoles.set(Calendar.SECOND, 0);
        calendarProximoMiercoles.set(Calendar.MILLISECOND, 0);
        if (calendarHoy.after(calendarProximoMiercoles)) {
            calendarProximoMiercoles.add(Calendar.DATE, 7);
        }
        return calendarProximoMiercoles.getTimeInMillis() - calendarHoy.getTimeInMillis();
    }
    private void scheduleNotification2(long delayMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificacionesActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent);
        }
    }
}