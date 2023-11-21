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
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
        // Cargar y aplicar el estado del Switch
        boolean switchState1 = loadSwitchState();
        switchUno.setChecked(switchState1);
        // Habilitar o deshabilitar las opciones y la imagen según el estado del Switch
        updateOptionsAndImageEnabled1(switchState1);
        tuerquita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verificar si el Switch está habilitado antes de abrir la ventana de opciones
                if (switchUno.isChecked()) {
                    // Mostrar el cuadro de diálogo con opciones
                    showOptionsDialog1();
                }
            }
        });
        // Escuchar cambios en el Switch
        switchUno.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            // Guardar el estado del Switch
            saveSwitchState1(isChecked1);

            // Habilitar o deshabilitar las opciones y la imagen según el estado del Switch
            updateOptionsAndImageEnabled1(isChecked1);
        });
        boolean switchDosState = loadSwitchDosState();
        switchDos.setChecked(switchDosState);

        // Habilitar o deshabilitar las opciones y la imagen según el estado del SwitchDos
        updateOptionsAndImageEnabled2(switchDosState);
        switchDos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Guardar el estado del SwitchDos
                saveSwitchDosState(isChecked);
                saveSwitchState2(isChecked);
                updateNotificationEnabled(isChecked);

                // Habilitar o deshabilitar las opciones y la imagen según el estado del SwitchDos
                updateOptionsAndImageEnabled2(isChecked);

                // Llamar al método showWednesdayNotification2 si hoy es martes y SwitchDos está activado
                if (isChecked) {
                    Calendar calendarHoy = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
                    if (calendarHoy.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                        showWednesdayNotification2();
                    }

                    // Llamar al método showNotificationOptionsDialog solo si SwitchDos está activado
                    showOptionsDialog2();
                }

                // Llamar al método scheduleNotifications() aquí
                scheduleNotifications();
            }
        });
    }
    private void showOptionsDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona opciones");

        // Lista de opciones (puedes personalizar esto según tus necesidades)
        String[] options = {"Aatrox", "Ahri", "Akali", "Akshan", "Alistar", "Amumu", "Anivia", "Annie", "Aphelios", "Ashe", "Azir", "Aurelion Sol", "Bardo", "Bel'Veth", "Blitzcrank", "Brand", "Briar", "Caitlyn", "Camille", "Cassiopeia", "Cho'Gath", "Corki", "Darius", "Diana", "Draven", "Dr.Mundo", "Ekko", "Elise", "Evelynn", "Ezreal", "Fiddlesticks", "Fiora", "Galio", "Gankplank", "Gnar", "Gragas", "Graves", "Gwen", "Hecarim", "Heimerdinger", "Illaoi", "Irelia", "Ivern", "Janna", "JarvanIV", "Jax", "Jayce", "Jhin", "Jinx", "Kai'sa", "Kalista", "Karma", "Karthus", "Kassadin", "Katarina", "Kayle", "Kayn", "Kennen", "Kled", "Kog'Maw", "Lillia", "Lissandra", "Lucian", "Lulu", "Lux", "Maestro Yi", "Malzahar", "Maokai", "Milio", "Miss Fortune", "Mordekaiser", "Morgana", "Naafiri", "Nami", "Nasus", "Nautilus", "Neeko", "Nidalee", "Nilah", "Nocturne", "Nunu y Willump", "Olaf", "Orianna", "Ornn", "Pantheon", "Poppy", "Pyke", "Qiyana", "Quinn", "Rakan", "Rammus", "Rek'Sai", "Rell", "Renata Glasc", "Renekton", "Riven", "Rumble", "Ryze", "Samira", "Sejuani", "Senna", "Seraphine", "Sett", "Shaco", "Shen", "Shyvana", "Singed", "Sion", "Sivir", "Sona", "Soraka", "Swain", "Sylas", "Syndra", "Tahm Kenck", "Taliyah", "Talon", "Taric", "Teemo", "Thresh", "Tristana", "Trundle", "Tryndamere", "Twisted Fate", "Twitch", "Udyr", "Urgot", "Varus", "Vayne", "Veigar", "Vel'Koz", "Vex", "Vi", "Viego", "Viktor", "Vladimir", "Volibear", "Warwick", "Wukong", "Xayah", "Xerath", "Xin Zhao", "Yasuo", "Yuumi", "Zed", "Zeri", "Ziggs", "Zilean", "Zoe", "Zyra"};

        // Estado de selección para cada opción
        boolean[] selectedOptions = loadSelectedOptions(); // Cargar el estado de selección guardado

        builder.setMultiChoiceItems(options, selectedOptions, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                // Actualizar el estado de selección al hacer clic en una opción
                selectedOptions[i] = isChecked;
            }
        });
        builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Guardar el estado de selección al hacer clic en "Listo"
                saveSelectedOptions(selectedOptions);

                // Realizar acciones con las opciones seleccionadas
                // En este ejemplo, simplemente imprimir las opciones seleccionadas
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
                // Cerrar el cuadro de diálogo
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    // Métodos para guardar y cargar el estado del Switch y las opciones
    private void saveSwitchState1(boolean switchState1) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("switch_state", switchState1);
        editor.apply();
    }

    private boolean loadSwitchState() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("switch_state", false); // Valor predeterminado: false
    }

    private void saveSelectedOptions(boolean[] selectedOptions) {
        // Usar SharedPreferences para almacenar el estado de selección
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
        // Cargar el estado de selección guardado desde SharedPreferences para tuerquita2
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selectedSet = preferences.getStringSet("selected_options2", new HashSet<>());

        boolean[] selectedOptions = new boolean[2]; // Ajustar esto según el número de opciones
        for (String index : selectedSet) {
            int i = Integer.parseInt(index);
            if (i >= 0 && i < selectedOptions.length) {
                selectedOptions[i] = true;
            }
        }
        return selectedOptions;
    }
    private boolean[] loadSelectedOptions() {
        // Cargar el estado de selección guardado desde SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selectedSet = preferences.getStringSet("selected_options", new HashSet<>());

        boolean[] selectedOptions = new boolean[156]; // Ajustar esto según el número de opciones
        for (String index : selectedSet) {
            int i = Integer.parseInt(index);
            if (i >= 0 && i < selectedOptions.length) {
                selectedOptions[i] = true;
            }
        }
        return selectedOptions;
    }
    private void updateOptionsAndImageEnabled1(boolean isEnabled) {
        // Habilitar o deshabilitar las opciones y la imagen según el estado del Switch
        if (isEnabled) {
            Log.d("Switch", "Switch habilitado");
        } else {
            Log.d("Switch", "Switch deshabilitado");
        }
        // Deshabilitar la imagen si el Switch está deshabilitado
        tuerquita.setEnabled(isEnabled);
    }
    private void showWednesdayNotification() {
        // Obtén la zona horaria de Chile
        TimeZone timeZoneChile1 = TimeZone.getTimeZone("Chile/Continental");
        // Obtén la fecha y hora actual en la zona horaria de Chile
        Calendar calendar = Calendar.getInstance(timeZoneChile1);
        // Verifica si hoy es martes y el switch está activado
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && switchUno.isChecked()) {
            // Obtén las opciones seleccionadas
            boolean[] selectedOptions = loadSelectedOptions();

            // Verifica si al menos una opción está seleccionada
            boolean atLeastOneOptionSelected = false;
            for (boolean option : selectedOptions) {
                if (option) {
                    atLeastOneOptionSelected = true;
                    break;
                }
            }
            // Si al menos una opción está seleccionada, programa la notificación para el miércoles a las 00:00
            if (atLeastOneOptionSelected) {
                // Obtén el tiempo hasta el próximo miércoles a las 00:00
                long tiempoRestanteHastaMiercoles = calculateTimeUntilNextWednesday();
                // Programa la notificación para el miércoles a las 00:00
                scheduleNotification(tiempoRestanteHastaMiercoles);
            }
        }
    }
    private void scheduleDailyVibrationNotifications() {
        // Obtener la zona horaria de Chile
        TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
        // Obtener la fecha y hora actual en la zona horaria de Chile
        Calendar calendarActual = Calendar.getInstance(timeZoneChile);
        // Establecer la hora de inicio para el miércoles a las 00:00
        Calendar calendarInicio = Calendar.getInstance(timeZoneChile);
        calendarInicio.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendarInicio.set(Calendar.HOUR_OF_DAY, 0);
        calendarInicio.set(Calendar.MINUTE, 0);
        calendarInicio.set(Calendar.SECOND, 0);
        calendarInicio.set(Calendar.MILLISECOND, 0);
        // Si hoy es después del miércoles, avanzar al próximo miércoles
        if (calendarActual.after(calendarInicio)) {
            calendarInicio.add(Calendar.DATE, 7);
        }
        // Establecer la hora de fin para el lunes a las 00:00
        Calendar calendarFin = (Calendar) calendarInicio.clone();
        calendarFin.add(Calendar.DATE, 5); // 5 días después (hasta el lunes)
        // Programar notificaciones diarias desde el miércoles hasta el lunes
        while (calendarInicio.before(calendarFin)) {
            // Programar notificación para la fecha actual
            scheduleNotification(calendarInicio.getTimeInMillis());

            // Avanzar al siguiente día
            calendarInicio.add(Calendar.DATE, 1);
        }
        // Deshabilitar lo guardado en la ventana que abre tuerquita2
        disableSavedOptions();
    }
    private long calculateTimeUntilNextWednesday() {
        // Obtén la fecha y hora actual en la zona horaria de Chile
        Calendar calendarHoy = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        // Ajustes para el próximo miércoles
        Calendar calendarProximoMiercoles = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        calendarProximoMiercoles.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendarProximoMiercoles.set(Calendar.HOUR_OF_DAY, 0);
        calendarProximoMiercoles.set(Calendar.MINUTE, 0);
        calendarProximoMiercoles.set(Calendar.SECOND, 0);
        calendarProximoMiercoles.set(Calendar.MILLISECOND, 0);
        // Si hoy es después del miércoles, avanzar al próximo miércoles
        if (calendarHoy.after(calendarProximoMiercoles)) {
            calendarProximoMiercoles.add(Calendar.DATE, 7);
        }
        // Calcular el tiempo hasta el próximo miércoles a las 00:00
        return calendarProximoMiercoles.getTimeInMillis() - calendarHoy.getTimeInMillis();
    }
    private void showOptionsDialog2() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Selecciona opciones para tuerquita2");

            // Lista de opciones para tuerquita2
            String[] options = {"Notificar todos los días", "Notificar el último día"};
            // Estado de selección para la opción actual
            int selectedOption = loadSelectedOption2();
            builder.setSingleChoiceItems(options, selectedOption, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        // Actualizar el estado de selección al hacer clic en una opción
                        saveSelectedOption2(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Manejar la excepción según tus necesidades
                    }
                }
            });
            builder.setPositiveButton("Listo", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        // Obtener la opción seleccionada
                        int selectedOption = loadSelectedOption2();

                        // Realizar acciones según la opción seleccionada para tuerquita2
                        handleNotificationOption(selectedOption);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Manejar la excepción según tus necesidades
                    }
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        // Cerrar el cuadro de diálogo
                        dialogInterface.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Manejar la excepción según tus necesidades
                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
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
        return preferences.getInt("selected_option2", 0); // Valor predeterminado: 0
    }
    private void scheduleNotification(long delayMillis) {
        // Utiliza un AlarmManager para programar la notificación
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Reemplaza NotificacionReceiver con tu clase de receptor de notificaciones
        Intent intent = new Intent(this, NotificacionesActivity.class);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Programa la notificación para el tiempo especificado
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent);
        }
    }
    private void handleNotificationOption(int selectedOption) {
        try {
            switch (selectedOption) {
                case 0:
                    // Notificar todos los días
                    scheduleDailyNotificationsForTuerquita2();
                    break;
                case 1:
                    // Notificar el último día (martes)
                    scheduleTuesdayNotification();
                    break;
                default:
                    // Opción no reconocida
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }
    }

    private void scheduleDailyNotificationsForTuerquita2() {
        // Obtén el servicio de alarma
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // Configura el intent para el receptor de la alarma
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Configura el tiempo de inicio de la alarma (miércoles a las 00:00)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Asegúrate de que la alarma se configure para la próxima semana si hoy ya es miércoles
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DATE, 7); // Agrega 7 días para ir al próximo miércoles
        }
        // Configura la alarma para repetirse cada día
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
            // Código para notificación con vibración
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Comprobar si la versión de Android es Oreo o superior
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Crear un canal de notificación para versiones de Android Oreo y superiores
                NotificationChannel channel = new NotificationChannel("channel_id", "Nombre del canal", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableVibration(true); // Habilitar la vibración en el canal
                notificationManager.createNotificationChannel(channel);
            }
            // Configurar la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setContentTitle("Título de la notificación")
                    .setContentText("Contenido de la notificación")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            // Muestra la notificación
            notificationManager.notify(1, builder.build());
        }
    }
    private void updateOptionsAndImageEnabled2(boolean isEnabled) {
        if (isEnabled) {
            Log.d("Tuerquita2", "Opciones e imagen habilitadas");

            // Habilitar el click en la imagen "tuerquita2"
            tuerquita2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Verificar si el SwitchDos está habilitado antes de abrir la ventana de opciones
                    if (switchDos.isChecked()) {
                        // Mostrar el cuadro de diálogo con opciones
                        showOptionsDialog2();
                    }
                }
            });
        } else {
            Log.d("Tuerquita2", "Opciones e imagen deshabilitadas");

            // Deshabilitar el click en la imagen "tuerquita2"
            tuerquita2.setOnClickListener(null);
        }
    }
    private void scheduleTuesdayNotification() {
        try {
            // Obtener la zona horaria de Chile
            TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
            // Obtener la fecha y hora actual en la zona horaria de Chile
            Calendar calendarHoy = Calendar.getInstance(timeZoneChile);
            // Ajustes para el próximo martes
            Calendar calendarProximoMartes = Calendar.getInstance(timeZoneChile);
            calendarProximoMartes.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            calendarProximoMartes.set(Calendar.HOUR_OF_DAY, 0);
            calendarProximoMartes.set(Calendar.MINUTE, 0);
            calendarProximoMartes.set(Calendar.SECOND, 0);
            calendarProximoMartes.set(Calendar.MILLISECOND, 0);

            // Si hoy es después del martes, avanzar al próximo martes
            if (calendarHoy.after(calendarProximoMartes)) {
                calendarProximoMartes.add(Calendar.DATE, 7);
            }
            // Calcular el tiempo hasta el próximo martes a las 00:00
            long tiempoRestanteHastaMartes = calendarProximoMartes.getTimeInMillis() - calendarHoy.getTimeInMillis();
            // Programar la notificación para el próximo martes a las 00:00 con vibración
            scheduleNotification(tiempoRestanteHastaMartes, true);
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }
    }
    private void scheduleNotification(long delayMillis, boolean withVibration) {
        // Utilizar un AlarmManager para programar la notificación
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Reemplazar NotificacionReceiver con tu clase de receptor de notificaciones
        Intent intent = new Intent(this, NotificacionesActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Configurar opciones de notificación
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

        // Programar la notificación para el tiempo especificado
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
    }
    private void scheduleNotifications() {
        try {
            // Verificar si el SwitchDos está habilitado
            if (switchDos.isChecked()) {
                // Obtener la opción guardada
                int savedNotificationOption = loadNotificationOption();

                // Lógica para programar notificaciones según la opción guardada
                if (savedNotificationOption == radioVibraciones.getId()) {
                    // Programar notificaciones con vibraciones diarias desde el miércoles hasta el lunes
                    scheduleDailyVibrationNotifications();
                } else if (savedNotificationOption == radioUltimoDia.getId()) {
                    // Programar notificación los martes como último día de la actualización de tienda
                    scheduleTuesdayNotification();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }
    }

    private void disableSavedOptions() {
        // Deshabilitar lo guardado en la ventana que abre tuerquita2
        // Puedes utilizar SharedPreferences para almacenar y recuperar el estado de las opciones

        // 1. Obtener el SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notification_option", 0); // 0 representa ninguna opción seleccionada
        editor.apply();
    }
    private int loadNotificationOption() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("notification_option", 0); // Valor predeterminado: 0
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
        // Lógica para habilitar o deshabilitar la notificación según el estado del SwitchDos
        if (isEnabled) {
            // Si el SwitchDos está habilitado, mostrar el cuadro de diálogo con opciones
            showOptionsDialog2();
        } else {
            // Si el SwitchDos está deshabilitado, deshabilitar lo guardado en la ventana que abre tuerquita2
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
        // Obtén la zona horaria de Chile
        TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
        // Obtén la fecha y hora actual en la zona horaria de Chile
        Calendar calendar = Calendar.getInstance(timeZoneChile);
        // Verifica si hoy es martes y el switch está activado
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY && switchDos.isChecked()) {
            // Obtén las opciones seleccionadas
            boolean[] selectedOptions = loadSelectedOptions();

            // Verifica si al menos una opción está seleccionada
            boolean atLeastOneOptionSelected = false;
            for (boolean option : selectedOptions) {
                if (option) {
                    atLeastOneOptionSelected = true;
                    break;
                }
            }
            // Si al menos una opción está seleccionada, programa la notificación para el miércoles a las 00:00
            if (atLeastOneOptionSelected) {
                // Obtén el tiempo hasta el próximo miércoles a las 00:00
                long tiempoRestanteHastaMiercoles2 = calculateTimeUntilNextWednesday2();

                // Programa la notificación para el miércoles a las 00:00
                scheduleNotification2(tiempoRestanteHastaMiercoles2);
            }
        }
    }
    private long calculateTimeUntilNextWednesday2() {
        // Obtén la fecha y hora actual en la zona horaria de Chile
        Calendar calendarHoy = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        // Ajustes para el próximo miércoles
        Calendar calendarProximoMiercoles = Calendar.getInstance(TimeZone.getTimeZone("Chile/Continental"));
        calendarProximoMiercoles.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calendarProximoMiercoles.set(Calendar.HOUR_OF_DAY, 0);
        calendarProximoMiercoles.set(Calendar.MINUTE, 0);
        calendarProximoMiercoles.set(Calendar.SECOND, 0);
        calendarProximoMiercoles.set(Calendar.MILLISECOND, 0);
        // Si hoy es después del miércoles, avanzar al próximo miércoles
        if (calendarHoy.after(calendarProximoMiercoles)) {
            calendarProximoMiercoles.add(Calendar.DATE, 7);
        }
        // Calcular el tiempo hasta el próximo miércoles a las 00:00
        return calendarProximoMiercoles.getTimeInMillis() - calendarHoy.getTimeInMillis();
    }
    private void scheduleNotification2(long delayMillis) {
        // Utiliza un AlarmManager para programar la notificación
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, NotificacionesActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Programa la notificación para el tiempo especificado
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delayMillis, pendingIntent);
        }
    }
}