package com.databit.skinslol2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.TextView;

import com.databit.skinslol2.MainActivity;

public class ParcheActivity extends AppCompatActivity {

    Button btnParche1;
    Button btnParche2;
    Button btnParche3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parche);

        btnParche1=(Button) findViewById(R.id.btnMainParche);
        btnParche1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btn1 = new Intent(ParcheActivity.this, ParcheActivity.class);
                startActivity(btn1);
            }
        });
        btnParche2=(Button) findViewById(R.id.btnNotificacionesParche);
        btnParche2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnParche2 = new Intent(ParcheActivity.this, NotificacionesActivity.class);
                startActivity(btnParche2);
            }
        });
        btnParche3=(Button) findViewById(R.id.btnOfertasParche);
        btnParche3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnParche3 = new Intent(ParcheActivity.this, MainActivity.class);
                startActivity(btnParche3);
            }
        });
        TextView txtPagina = findViewById(R.id.txtPagina);

        // Agrega un OnClickListener al TextView
        txtPagina.setOnClickListener(view -> {
            // Obtén la URL del TextView
            String url = txtPagina.getText().toString();

            // Crea un Intent para abrir la URL en un navegador web
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            // Verifica si hay aplicaciones que pueden manejar la acción (abrir una URL)
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Inicia la actividad para abrir la URL
                startActivity(intent);
            }
        });
    }

}
