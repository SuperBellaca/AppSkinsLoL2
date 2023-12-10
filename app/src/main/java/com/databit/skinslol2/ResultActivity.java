package com.databit.skinslol2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedItem") && intent.hasExtra("linearLayoutId")) {
            String selectedItem = intent.getStringExtra("selectedItem");
            int linearLayoutId = intent.getIntExtra("linearLayoutId", 0);
            mostrarVistaSeleccionada(selectedItem, linearLayoutId);
        }
    }

    private void mostrarVistaSeleccionada(String selectedItem, int linearLayoutId) {
        LinearLayout linearLayout = findViewById(linearLayoutId);

        if (linearLayout != null) {
            ocultarTodosLosLayouts(linearLayoutId);

            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View child = linearLayout.getChildAt(i);
                child.setVisibility(View.VISIBLE);
            }
        } else {
            Toast.makeText(this, "No se encontró el LinearLayout correspondiente", Toast.LENGTH_SHORT).show();
        }
    }

    private void ocultarTodosLosLayouts(int exceptLayoutId) {
        LinearLayout mainLayout = findViewById(R.id.MainLayout);
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            View child = mainLayout.getChildAt(i);
            if (child instanceof LinearLayout && child.getId() != exceptLayoutId) {
                child.setVisibility(View.GONE);
            }
        }
    }
}
