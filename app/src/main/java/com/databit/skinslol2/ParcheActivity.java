package com.databit.skinslol2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.TextView;


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

        txtPagina.setOnClickListener(view -> {
            String url = txtPagina.getText().toString();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }

}
