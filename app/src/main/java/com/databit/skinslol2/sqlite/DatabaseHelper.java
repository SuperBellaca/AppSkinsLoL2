package com.databit.skinslol2.sqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "usuariosSkinsLoL.db";
    private static final int DATABASE_VERSION = 1;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_TIME_MILLIS = 60000;

    // Define la tabla y sus columnas
    public static final String TABLE_NAME = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_APELLIDO = "apellido";
    public static final String COLUMN_CORREO = "correo";
    public static final String COLUMN_CONTRASEÑA = "contrasena";
    public static final String COLUMN_INTENTOS_FALLIDOS = "intentos_fallidos";
    public static final String COLUMN_TIEMPO_DESBLOQUEO = "tiempo_desbloqueo";

    // Sentencia SQL para crear la tabla

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NOMBRE + " TEXT," +
                    COLUMN_APELLIDO + " TEXT," +
                    COLUMN_CORREO + " TEXT," +
                    COLUMN_CONTRASEÑA + " TEXT," +
                    COLUMN_INTENTOS_FALLIDOS + " INTEGER DEFAULT 0," +
                    COLUMN_TIEMPO_DESBLOQUEO + " INTEGER DEFAULT 0)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL( "DROP TABLE " + TABLE_NAME);
        //onCreate(db);

    }

    public boolean verificarCredenciales(String correo, String contrasena) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Consulta para verificar si las credenciales existen
            String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_CORREO + " = ?" +
                    " AND " + COLUMN_CONTRASEÑA + " = ?";
            cursor = db.rawQuery(query, new String[]{correo, contrasena});

            // Verifica si el cursor tiene exactamente un resultado
            boolean credencialesValidas = cursor.getCount() == 1;

            // Si las credenciales son válidas, reinicia el contador de intentos
            if (credencialesValidas) {
                reiniciarContadorIntentos(correo);
            }

            return credencialesValidas;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    private void incrementarContadorIntentos(String correo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Incrementa el contador de intentos fallidos
            db.execSQL("UPDATE " + TABLE_NAME +
                    " SET " + COLUMN_INTENTOS_FALLIDOS + " = " + COLUMN_INTENTOS_FALLIDOS + " + 1" +
                    " WHERE " + COLUMN_CORREO + " = ?", new String[]{correo});

            // Si se alcanza el límite, bloquea la cuenta temporalmente
            if (obtenerContadorIntentos(correo) >= MAX_FAILED_ATTEMPTS) {
                bloquearCuentaTemporalmente(correo);
            }
        } finally {
            db.close();
        }
    }

    private int obtenerContadorIntentos(String correo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Consulta para obtener el contador actual de intentos fallidos
            String query = "SELECT " + COLUMN_INTENTOS_FALLIDOS +
                    " FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_CORREO + " = ?";
            cursor = db.rawQuery(query, new String[]{correo});

            int intentosFallidos = 0;
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(COLUMN_INTENTOS_FALLIDOS);
                if (columnIndex != -1) {
                    intentosFallidos = cursor.getInt(columnIndex);
                }
            }

            return intentosFallidos;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    private void bloquearCuentaTemporalmente(String correo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Obtén la hora actual
            long currentTimeMillis = System.currentTimeMillis();

            // Calcula el tiempo de desbloqueo (tiempo actual + tiempo de bloqueo)
            long unlockTimeMillis = currentTimeMillis + LOCKOUT_TIME_MILLIS;

            // Almacena el tiempo de desbloqueo en la base de datos
            ContentValues values = new ContentValues();
            values.put(COLUMN_TIEMPO_DESBLOQUEO, unlockTimeMillis);
            db.update(TABLE_NAME, values, COLUMN_CORREO + " = ?", new String[]{correo});

        } finally {
            db.close();
        }
    }

    private void reiniciarContadorIntentos(String correo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            // Reinicia el contador de intentos fallidos en la base de datos
            ContentValues values = new ContentValues();
            values.put(COLUMN_INTENTOS_FALLIDOS, 0);
            db.update(TABLE_NAME, values, COLUMN_CORREO + " = ?", new String[]{correo});
        } finally {
            db.close();
        }
    }
}