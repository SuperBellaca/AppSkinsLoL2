package com.databit.skinslol2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

    Button btn2;
    Button btn3;
    TextView txtTemp;
    private CountDownTimer countDownTimer;
    private long tiempoRestanteMilisegundos;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate: Iniciando MainActivity...");
        try {
            super.onCreate(savedInstanceState);

            Log.d("MainActivity", "onCreate: Verificando sesión...");
            setContentView(R.layout.activity_main);
            btn2 = findViewById(R.id.btnParche);
            btn3 = findViewById(R.id.btnNotificaciones);
            txtTemp = findViewById(R.id.txtTemp);
            ImageButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
            ImageButton btnPerfil = findViewById(R.id.btnPerfil);
            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.editTextBuscar);

            btn2.setOnClickListener(view -> {
                Log.d("MainActivity", "Clic en btn2");
                Intent btn2Intent = new Intent(MainActivity.this, ParcheActivity.class);
                startActivity(btn2Intent);
            });
            btn3.setOnClickListener(view -> {
                Log.d("MainActivity", "Clic en btn3");
                Intent btn3Intent = new Intent(MainActivity.this, NotificacionesActivity.class);
                startActivity(btn3Intent);
            });
            btnPerfil.setOnClickListener(view -> {
                Log.d("MainActivity", "Clic perfil");
                Intent btn3Intent = new Intent(MainActivity.this, PerfilActivity.class);
                startActivity(btn3Intent);
            });
            btnCerrarSesion.setOnClickListener(v -> showLogoutDialog());

            TimeZone timeZoneChile = TimeZone.getTimeZone("Chile/Continental");
            Calendar calHoy = Calendar.getInstance(timeZoneChile);
            Calendar calProximoMiercoles = Calendar.getInstance(timeZoneChile);
            calProximoMiercoles.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            calProximoMiercoles.set(Calendar.HOUR_OF_DAY, 0);
            calProximoMiercoles.set(Calendar.MINUTE, 0);
            calProximoMiercoles.set(Calendar.SECOND, 0);
            calProximoMiercoles.set(Calendar.MILLISECOND, 0);
            if (calHoy.after(calProximoMiercoles)) {
                calProximoMiercoles.add(Calendar.DATE, 7);
            }
            long tiempoRestanteHastaMiercoles = calProximoMiercoles.getTimeInMillis() - calHoy.getTimeInMillis();
            iniciarCronometro(tiempoRestanteHastaMiercoles);
            String[] tuListaDeSugerencias = {"Kai'Sa Tinta Sombría", "Ezreal Gallardo", "Rakan Pacto Quebrantado","Neeko Guardiana Estelar","AstroNautilus","Anivia Prehistórica","Tahm Kench Master Chef","Nunu y Willump Demoledor","Maestro Yi PsyOps","Jayce Academia de Combate"};
            ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tuListaDeSugerencias);
            autoCompleteTextView.setAdapter(adaptador);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adaptadorVista, View vista, int posicion, long id) {
                    String elementoSeleccionado = adaptadorVista.getItemAtPosition(posicion).toString();
                    lanzarResultActivity(elementoSeleccionado);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error en onCreate: " + e.getMessage());
        }
    }
    private void saveSessionState(boolean isActive) {
        SharedPreferences preferences = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_active", isActive);
        editor.apply();
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?");

        builder.setPositiveButton("Sí", (dialog, which) -> {
            saveSessionState(false);
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
    private void lanzarResultActivity(String selectedItem) {
        int linearLayoutId = obtenerIdLinearLayout(selectedItem);

        if (linearLayoutId != 0) {
            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("selectedItem", selectedItem);
            intent.putExtra("linearLayoutId", linearLayoutId);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No se encontró el LinearLayout correspondiente", Toast.LENGTH_SHORT).show();
        }
    }

    private int obtenerIdLinearLayout(String selectedItem) {
        switch (selectedItem) {
            case "Kai'Sa Tinta Sombría":
                return R.id.KaisaTintaSombriaLayout;
            case "Ezreal Gallardo":
                return R.id.EzrealGallardoLayout;
            case "Rakan Pacto Quebrantado":
                return R.id.RakanPactoQuebrantadoLayout;
            case "Neeko Guardiana Estelar":
                return R.id.NeekoLayout;
            case "AstroNautilus":
                return R.id.AstroNautilusLayout;
            case "Anivia Prehistórica":
                return R.id.AniviaPrehistoricaLayout;
            case "Tahm Kench Master Chef":
                return R.id.TahmKenchLayout;
            case "Nunu y Willump Demoledor":
                return R.id.NunuyWillumpLayout;
            case "Maestro Yi PsyOps":
                return R.id.MaestroYiLayout;
            case "Jayce Academia de Combate":
                return R.id.JayceAcedemiaLayout;
            default:
                return 0;
        }
    }
    private void filterContent(String query) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                String text = textView.getText().toString().toLowerCase();

                if (text.contains(query.toLowerCase())) {
                    child.setVisibility(View.VISIBLE);
                } else {
                    child.setVisibility(View.GONE);
                }
            } else if (child instanceof ImageView) {
                ImageView imageView = (ImageView) child;
                String contentDescription = imageView.getContentDescription().toString().toLowerCase();

                if (contentDescription.contains(query.toLowerCase())) {
                    child.setVisibility(View.VISIBLE);
                } else {
                    child.setVisibility(View.GONE);
                }
            }
        }
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
        filterContent(newText);
        return false;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
