package com.databit.skinslol2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.databit.skinslol2.sqlite.DatabaseHelper;
import android.widget.Toast;
import android.view.View;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.databit.skinslol2.sqlite.Usuario;
import android.content.Intent;

public class CuentaActivity extends AppCompatActivity {
    private EditText editTxtNombre;
    private EditText editTxtApellido;
    private EditText editTxtCorreo;
    private EditText editTxtContra;
    private Button btnCuenta;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        TextView txtNombre = findViewById(R.id.txtNombreCuenta);
        TextView txtApellido = findViewById(R.id.txtApellidoCuenta);
        TextView txtCorreo = findViewById(R.id.txtCorreoCuenta);
        TextView txtContra = findViewById(R.id.txtContraCuenta);

        editTxtNombre = findViewById(R.id.editTxtNombreCuenta);
        editTxtApellido = findViewById(R.id.editTxtApellidoCuenta);
        editTxtCorreo = findViewById(R.id.editTxtCorreoCuenta);
        editTxtContra = findViewById(R.id.editTxtContraCuenta);
        btnCuenta = findViewById(R.id.btnCuenta);

        dbHelper = new DatabaseHelper(this);
        btnCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarDatos(v);
            }
        });
    }

    public void guardarDatos(View view) {
        // Obtener datos de los EditText
        String nombre = editTxtNombre.getText().toString();
        String apellido = editTxtApellido.getText().toString();
        String correo = editTxtCorreo.getText().toString();
        String contrasena = editTxtContra.getText().toString();
        // Verificar si el correo ya está registrado

        if (correoExistente(correo)) {
            // Mostrar un mensaje de error y salir del método
            Toast.makeText(this, "Correo ya registrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el correo es una dirección de Gmail válida
        if (!esCorreoGmailValido(correo)) {
            // Mostrar un mensaje de error y salir del método
            Toast.makeText(this, "Correo no es una dirección de Gmail válida", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario nuevoUsuario = new Usuario(-1, nombre, apellido, correo, contrasena);

        // Crear instancia de DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Obtener base de datos en modo escritura
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los valores que se insertarán
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOMBRE, nombre);
        values.put(DatabaseHelper.COLUMN_APELLIDO, apellido);
        values.put(DatabaseHelper.COLUMN_CORREO, correo);
        values.put(DatabaseHelper.COLUMN_CONTRASEÑA, contrasena);

        try {
            // Insertar el nuevo usuario en la base de datos
            db.insertOrThrow(DatabaseHelper.TABLE_NAME, null, values);

            // Mostrar un mensaje de éxito
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();

            // Redireccionar a LoginActivity después de guardar los datos
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Esto finaliza la actividad actual para que el usuario no pueda regresar presionando "Atrás"
        } catch (SQLException e) {
            // Manejar errores de inserción
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
        } finally {
            // Cerrar la conexión a la base de datos en el bloque finally
            db.close();
        }
    }
    private boolean correoExistente(String correo) {
        // Verificar si el correo ya existe en la base de datos
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME +
                    " WHERE " + DatabaseHelper.COLUMN_CORREO + " = ?";
            cursor = db.rawQuery(query, new String[]{correo});

            // Si el cursor tiene algún resultado, significa que el correo ya está registrado
            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private boolean esCorreoGmailValido(String correo) {
        // Utilizar una expresión regular para verificar si el correo es de Gmail
        String patronGmail = "[a-zA-Z0-9._%+-]+@gmail\\.com";
        return correo.matches(patronGmail);
    }
}

