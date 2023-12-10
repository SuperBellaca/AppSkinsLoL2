package com.databit.skinslol2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CuentaActivity extends AppCompatActivity {
    private EditText editTxtNombre;
    private EditText editTxtApellido;
    private EditText editTxtCorreo;
    private EditText editTxtContra;
    private Button btnCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        editTxtNombre = findViewById(R.id.editTxtNombreCuenta);
        editTxtApellido = findViewById(R.id.editTxtApellidoCuenta);
        editTxtCorreo = findViewById(R.id.editTxtCorreoCuenta);
        editTxtContra = findViewById(R.id.editTxtContraCuenta);
        btnCuenta = findViewById(R.id.btnCuenta);

        btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCuentaFirebase();
            }
        });
    }

    private void crearCuentaFirebase() {

        final String nombre = editTxtNombre.getText().toString().trim();
        final String apellido = editTxtApellido.getText().toString().trim();
        final String correo = editTxtCorreo.getText().toString().trim();
        String contrasena = editTxtContra.getText().toString().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (correoExistente(correo)) {
            Toast.makeText(this, "Correo ya registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!esCorreoGmailValido(correo)) {

            Toast.makeText(this, "Correo no es una dirección de Gmail válida", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(CuentaActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference usuarioReference = databaseReference.child(uid);

                            usuarioReference.child("nombre").setValue(nombre);
                            usuarioReference.child("apellido").setValue(apellido);
                            usuarioReference.child("correo").setValue(correo);
                            usuarioReference.child("contrasena").setValue(contrasena);


                            redirigirALogin();
                        } else {
                            Toast.makeText(CuentaActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean correoExistente(String correo) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        Task<DataSnapshot> task = databaseReference.orderByChild("correo").equalTo(correo).get();

        try {
            return task.getResult().exists();
        } catch (Exception e) {
            return false;
        }
    }
    private void redirigirALogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean esCorreoGmailValido(String correo) {
        String patronGmail = "[a-zA-Z0-9._%+-]+@gmail\\.com";
        return correo.matches(patronGmail);
    }
}
