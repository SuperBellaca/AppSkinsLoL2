package com.databit.skinslol2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout linearLayoutMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView txtCuentaNueva = findViewById(R.id.txtCuentaNueva);
        LinearLayout linearLayoutMain = findViewById(R.id.linearLayoutmain);

        txtCuentaNueva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CuentaActivity.class);
                startActivity(intent);
            }
        });
        linearLayoutMain.setVisibility(View.VISIBLE);
    }
    private void hideLayout() {
        linearLayoutMain.setVisibility(View.INVISIBLE);
    }
    public void onButtonClick(View view) {
        hideLayout();
    }
    private String obtenerCorreo() {
        EditText editTxtCorreo = findViewById(R.id.editTxtCorreo);
        return editTxtCorreo.getText().toString();
    }

    private String obtenerContrasena() {
        EditText editTxtContrasena = findViewById(R.id.editTxtPassword);
        return editTxtContrasena.getText().toString();
    }

    public void iniciarSesion(View view) {
        final String correo = obtenerCorreo();
        final String contrasena = obtenerContrasena();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
