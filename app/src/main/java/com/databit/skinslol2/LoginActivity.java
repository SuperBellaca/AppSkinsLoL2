package com.databit.skinslol2;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.databit.skinslol2.sqlite.DatabaseHelper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Obtener referencia al TextView
        TextView txtCuentaNueva = findViewById(R.id.txtCuentaNueva);

// Establecer OnClickListener para el TextView
        txtCuentaNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la CuentaActivity
                Intent intent = new Intent(LoginActivity.this, CuentaActivity.class);
                startActivity(intent);
            }
        });

    }

    private String obtenerCorreo() {
        // Supongamos que tienes un EditText llamado editTxtCorreo
        EditText editTxtCorreo = findViewById(R.id.editTxtCorreo);
        return editTxtCorreo.getText().toString();
    }

    private String obtenerContrasena() {
        // Supongamos que tienes un EditText llamado editTxtContrasena
        EditText editTxtContrasena = findViewById(R.id.editTxtPassword);
        return editTxtContrasena.getText().toString();
    }

    public void iniciarSesion(View view) {
        final String correo = obtenerCorreo();
        final String contrasena = obtenerContrasena();

        // Utilizar Executor para la verificación en segundo plano
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Verificar credenciales en la base de datos
                DatabaseHelper dbHelper = new DatabaseHelper(LoginActivity.this);
                final boolean credencialesValidas = dbHelper.verificarCredenciales(correo, contrasena);

                // Actualizar la interfaz de usuario en el hilo principal
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (credencialesValidas) {
                            // Credenciales válidas, abrir la MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Finalizar LoginActivity para evitar volver atrás
                        } else {
                            // Credenciales no válidas, mostrar un mensaje de error
                            Toast.makeText(LoginActivity.this, "Credenciales no válidas", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}