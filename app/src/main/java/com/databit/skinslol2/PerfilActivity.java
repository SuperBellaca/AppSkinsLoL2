package com.databit.skinslol2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class PerfilActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private FirebaseStorage storage;
    private ImageView imageViewPerfil;
    private ImageView imgCambiarFoto;
    private ImageView imgViewVolver;
    private TextView Correo;
    private TextView Nombre;
    private TextView Apellido;
    private Button btnEditarPerfil;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int EDITAR_PERFIL_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = firebaseAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        Correo = findViewById(R.id.txtCorreo);
        Nombre = findViewById(R.id.txtNombre);
        Apellido = findViewById(R.id.txtApellido);
        imgCambiarFoto = findViewById(R.id.imageViewLapiz);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        imgViewVolver = findViewById(R.id.imageViewVolver);

        cargarDatosPerfil();

        imgCambiarFoto.setOnClickListener(view -> seleccionarNuevaFoto());
        imgViewVolver.setOnClickListener(view -> {
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnEditarPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(PerfilActivity.this, EditarPerfilActivity.class);
            startActivityForResult(intent, EDITAR_PERFIL_REQUEST_CODE);
        });
    }


    public void actualizarInterfazUsuario(Usuario usuario) {
        Nombre.setText("Nombre: " + usuario.getNombre());
        Apellido.setText("Apellido: " + usuario.getApellido());
        Correo.setText("Correo: " + usuario.getCorreo());

        if (usuario.getUrlFotoPerfil() != null && !usuario.getUrlFotoPerfil().isEmpty()) {
            cargarImagenDesdeStorage(usuario.getUrlFotoPerfil());
        } else {
            imageViewPerfil.setImageResource(R.drawable.iconoperfil);
        }
    }


    private void cargarDatosPerfil() {
        DatabaseReference usuarioReference = databaseReference.child("usuarios").child(userId);
        usuarioReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);

                    if (usuario != null) {
                        Log.d("PerfilActivity", "Usuario obtenido correctamente: " + usuario.getNombre());

                        // Formatear los datos y mostrarlos en los TextView
                        Correo.setText("Correo: " + usuario.getCorreo());
                        Nombre.setText("Nombre: " + usuario.getNombre());
                        Apellido.setText("Apellido: " + usuario.getApellido());

                        if (usuario.getUrlFotoPerfil() != null && !usuario.getUrlFotoPerfil().isEmpty()) {
                            cargarImagenDesdeStorage(usuario.getUrlFotoPerfil());
                        } else {
                            imageViewPerfil.setImageResource(R.drawable.iconoperfil);
                        }
                    } else {
                        Log.d("PerfilActivity", "No se encontraron datos para el usuario con ID: " + userId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PerfilActivity", "Error al cargar datos del perfil: " + error.getMessage());
            }
        });
    }


    private void cargarImagenDesdeStorage(String imageUrl) {
        Picasso.get().invalidate(imageUrl);

        Picasso.get().load(imageUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(imageViewPerfil, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("PerfilActivity", "Imagen cargada exitosamente desde Storage");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("PerfilActivity", "Error al cargar la imagen desde Storage: " + e.getMessage());

                        imageViewPerfil.setImageResource(R.drawable.iconoperfil);
                    }
                });
    }

    private void seleccionarNuevaFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDITAR_PERFIL_REQUEST_CODE && resultCode == RESULT_OK) {
            // Actualizar datos del usuario después de editar el perfil
            cargarDatosPerfil();
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            subirNuevaFoto(filePath);
        }
    }

    private void subirNuevaFoto(Uri filePath) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child("perfil_imagenes/" + userId + "/imagen.jpg");

        UploadTask uploadTask = imageRef.putFile(filePath);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(this, "Imagen subida exitosamente", Toast.LENGTH_SHORT).show();

            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference usuarioReference = databaseReference.child("usuarios").child(userId);
                usuarioReference.child("urlFotoPerfil").setValue(uri.toString());

                Picasso.get().load(uri).into(imageViewPerfil, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("PerfilActivity", "Error al cargar la nueva imagen: " + e.getMessage());
                    }
                });
            }).addOnFailureListener(e -> {
                Log.e("PerfilActivity", "Error al obtener la URL de descarga: " + e.getMessage());
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            Log.e("PerfilActivity", "Error al subir la imagen: " + e.getMessage());
        });
    }

}