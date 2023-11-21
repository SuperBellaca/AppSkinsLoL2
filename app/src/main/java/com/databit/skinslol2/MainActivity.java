package com.databit.skinslol2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;
import android.widget.ImageButton;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Button btn1;
    Button btn2;
    Button btn3;
    TextView txtTemp;
    private CountDownTimer countDownTimer;
    private long tiempoRestanteMilisegundos;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.buscar);

        btn1 = findViewById(R.id.btnMain);
        btn2 = findViewById(R.id.btnParche);
        btn3 = findViewById(R.id.btnNotificaciones);
        txtTemp = findViewById(R.id.txtTemp);
        ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(v -> mostrarDialogoCerrarSesion());

        //Para navegar entre Activitys
        btn1.setOnClickListener(view -> {
            Intent btn1Intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(btn1Intent);
        });

        btn2.setOnClickListener(view -> {
            Intent btn2Intent = new Intent(MainActivity.this, ParcheActivity.class);
            startActivity(btn2Intent);
        });

        btn3.setOnClickListener(view -> {
            Intent btn3Intent = new Intent(MainActivity.this, NotificacionesActivity.class);
            startActivity(btn3Intent);
        });

        // Establece la zona horaria de Chile
        TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
        Calendar calHoy = Calendar.getInstance(timeZoneChile);
        Calendar calProximoMiercoles = Calendar.getInstance(timeZoneChile);

        // Ajustes para el cada miércoles
        calProximoMiercoles.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        calProximoMiercoles.set(Calendar.HOUR_OF_DAY, 0);
        calProximoMiercoles.set(Calendar.MINUTE, 0);
        calProximoMiercoles.set(Calendar.SECOND, 0);
        calProximoMiercoles.set(Calendar.MILLISECOND, 0);

        // Si hoy es después del miércoles, avanzar al próximo miércoles
        if (calHoy.after(calProximoMiercoles)) {
            calProximoMiercoles.add(Calendar.DATE, 7);
        }

        long tiempoRestanteHastaMiercoles = calProximoMiercoles.getTimeInMillis() - calHoy.getTimeInMillis();

        // Inicia el cronómetro
        iniciarCronometro(tiempoRestanteHastaMiercoles);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Mostrar el diálogo al hacer clic en "Cerrar Sesión"
            Log.d("MainActivity", "Clic en Cerrar Sesión"); // Agregar este mensaje de registro
            mostrarDialogoCerrarSesion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void mostrarDialogoCerrarSesion() {
        Toast.makeText(this, "Mostrar diálogo de cerrar sesión", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); // Cambia aquí
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Estás seguro de que deseas cerrar sesión?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
                    // Limpiar preferencias y luego iniciar LoginActivity
                    limpiarPreferencias();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // No hacer nada, simplemente cerrar el diálogo
                    dialog.dismiss();
                })
                .show();
    }

    private void limpiarPreferencias() {
        // Nombre de tu archivo de preferencias
        String prefFileName = "mis_preferencias";

        // Limpiar preferencias
        SharedPreferences preferences = getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Muestra un mensaje o realiza otras acciones necesarias
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }
    private void iniciarCronometro(long duracionTotal) {
        countDownTimer = new CountDownTimer(duracionTotal, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestanteMilisegundos = millisUntilFinished;
                actualizarTextoTemp();
            }

            @Override
            public void onFinish() {
                tiempoRestanteMilisegundos = 0;
                actualizarTextoTemp();

                // Reiniciar el cronómetro para el próximo miércoles
                iniciarCronometro(TimeUnit.DAYS.toMillis(7));
            }
        };

        countDownTimer.start();
    }

    private void actualizarTextoTemp() {
        long dias = TimeUnit.MILLISECONDS.toDays(tiempoRestanteMilisegundos);
        long horas = TimeUnit.MILLISECONDS.toHours(tiempoRestanteMilisegundos) % 24;
        long minutos = TimeUnit.MILLISECONDS.toMinutes(tiempoRestanteMilisegundos) % 60;
        long segundos = TimeUnit.MILLISECONDS.toSeconds(tiempoRestanteMilisegundos) % 60;

        String tiempoRestante = String.format(Locale.getDefault(), "La tienda se actualizará en: %d días, %d horas, %d minutos, %d segundos", dias, horas, minutos, segundos);
        txtTemp.setText(tiempoRestante);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Opcional: realiza acciones cuando el texto de búsqueda cambia
        filterContent(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Opcional: realiza acciones cuando se envía la consulta de búsqueda
        return false;
    }


    private void filterContent(String query) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);

            if (child instanceof TextView) {
                // Si el hijo es un TextView, filtra el texto
                TextView textView = (TextView) child;
                String text = textView.getText().toString().toLowerCase();

                if (text.contains(query.toLowerCase())) {
                    // El texto coincide, muestra la vista
                    child.setVisibility(View.VISIBLE);
                } else {
                    // El texto no coincide, oculta la vista
                    child.setVisibility(View.GONE);
                }
            } else if (child instanceof ImageView) {
                // Si el hijo es un ImageView, filtra por la descripción de contenido
                ImageView imageView = (ImageView) child;
                String contentDescription = imageView.getContentDescription().toString().toLowerCase();

                if (contentDescription.contains(query.toLowerCase())) {
                    // La descripción coincide, muestra la vista
                    child.setVisibility(View.VISIBLE);
                } else {
                    // La descripción no coincide, oculta la vista
                    child.setVisibility(View.GONE);
                }
            }
        }
    }
}